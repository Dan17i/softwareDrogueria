package com.drogueria.bellavista.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Limita intentos por IP en endpoints de autenticación (fuerza bruta, abuso de correos de reset).
 * Ventana deslizante en memoria: válido para una sola instancia de la app.
 * Usa getRemoteAddr(): no confía en X-Forwarded-For (spoofeable) salvo que el servidor lo reescriba.
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int DEFAULT_CLEANUP_THRESHOLD = 10_000;

    /** Límite de una ruta: máximo de peticiones dentro de la ventana. */
    public record Rule(int maxRequests, long windowMillis) {
    }

    private final Map<String, Rule> rules;
    private final LongSupplier clock;
    private final long maxWindowMillis;
    private final int cleanupThreshold;
    private final Map<String, Window> hits = new ConcurrentHashMap<>();

    public RateLimitFilter(Map<String, Rule> rules) {
        this(rules, System::currentTimeMillis);
    }

    RateLimitFilter(Map<String, Rule> rules, LongSupplier clock) {
        this(rules, clock, DEFAULT_CLEANUP_THRESHOLD);
    }

    RateLimitFilter(Map<String, Rule> rules, LongSupplier clock, int cleanupThreshold) {
        this.rules = Map.copyOf(rules);
        this.clock = clock;
        this.cleanupThreshold = cleanupThreshold;
        this.maxWindowMillis = this.rules.values().stream().mapToLong(Rule::windowMillis).max().orElse(0);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equals(request.getMethod()) || !rules.containsKey(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        Rule rule = rules.get(path);
        long now = clock.getAsLong();
        long retryAfterMillis = registerHit(request.getRemoteAddr() + "|" + path, rule, now);

        if (retryAfterMillis > 0) {
            log.warn("Rate limit excedido: ip={} path={}", request.getRemoteAddr(), path);
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf((retryAfterMillis + 999) / 1000));
            response.setContentType("application/json;charset=UTF-8");
            response.getOutputStream().write(
                    "{\"message\":\"Demasiados intentos. Intenta de nuevo más tarde.\"}".getBytes(StandardCharsets.UTF_8));
            return;
        }
        chain.doFilter(request, response);
    }

    /** Registra el intento. Retorna 0 si se permite, o los ms de espera si se excedió el límite. */
    private long registerHit(String key, Rule rule, long now) {
        if (hits.size() > cleanupThreshold) {
            hits.values().removeIf(w -> w.isStale(now, maxWindowMillis));
        }
        return hits.computeIfAbsent(key, k -> new Window()).register(rule, now);
    }

    /** Marcas de tiempo de una clave (IP + ruta). El acceso se serializa con synchronized sobre la propia instancia. */
    static final class Window {
        private final Deque<Long> timestamps = new ArrayDeque<>();

        synchronized long register(Rule rule, long now) {
            Long oldest = timestamps.peekFirst();
            while (oldest != null && now - oldest >= rule.windowMillis()) {
                timestamps.pollFirst();
                oldest = timestamps.peekFirst();
            }
            if (oldest != null && timestamps.size() >= rule.maxRequests()) {
                return rule.windowMillis() - (now - oldest);
            }
            timestamps.addLast(now);
            return 0;
        }

        synchronized boolean isStale(long now, long windowMillis) {
            Long last = timestamps.peekLast();
            return last == null || now - last >= windowMillis;
        }
    }
}
