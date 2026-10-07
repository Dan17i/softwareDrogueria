package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contrato de los POJOs escritos a mano (modelos de dominio y entidades JPA):
 * getters/setters, constructor completo, Builder, equals/hashCode/toString.
 * Detecta, por ejemplo, un campo nuevo que se olvidó en el Builder o en el constructor.
 */
@DisplayName("Contrato de POJOs (modelos y entidades)")
class PojoContractTest {

    private static final String MODEL = "com.drogueria.bellavista.domain.model.";
    private static final String PERSISTENCE = "com.drogueria.bellavista.infrastructure.persistence.";

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            MODEL + "Customer", MODEL + "GoodsReceipt", MODEL + "GoodsReceiptItem", MODEL + "Notification",
            MODEL + "Order", MODEL + "OrderItem", MODEL + "PasswordResetToken", MODEL + "Payment",
            MODEL + "Product", MODEL + "Supplier", MODEL + "User",
            PERSISTENCE + "CustomerEntity", PERSISTENCE + "GoodsReceiptEntity",
            PERSISTENCE + "GoodsReceiptItemEntity", PERSISTENCE + "OrderEntity",
            PERSISTENCE + "OrderItemEntity", PERSISTENCE + "PasswordResetTokenEntity",
            PERSISTENCE + "ProductEntity", PERSISTENCE + "SupplierEntity", PERSISTENCE + "UserEntity",
            PERSISTENCE + "entity.NotificationEntity", PERSISTENCE + "entity.PaymentEntity"
    })
    @DisplayName("getters, setters, constructor completo, Builder, equals, hashCode y toString son coherentes")
    void shouldHonorPojoContract(String className) throws Exception {
        Class<?> type = Class.forName(className);
        List<Field> fields = instanceFields(type);
        assertFalse(fields.isEmpty(), "Sin campos: " + className);

        // setters + getters
        Object bean = type.getDeclaredConstructor().newInstance();
        Map<String, Object> samples = new LinkedHashMap<>();
        for (Field f : fields) {
            Object sample = sampleFor(f.getType());
            samples.put(f.getName(), sample);
            invokeSetter(type, bean, f, sample);
        }
        for (Field f : fields) {
            assertEquals(samples.get(f.getName()), invokeGetter(type, bean, f), "getter " + f.getName());
        }

        // constructor completo
        Constructor<?> all = type.getDeclaredConstructor(fields.stream().map(Field::getType).toArray(Class[]::new));
        Object viaCtor = all.newInstance(samples.values().toArray());
        assertEquals(bean, viaCtor);

        // Builder
        Class<?> builderType = builderClass(type);
        Object builder = type.getMethod("builder").invoke(null);
        for (Field f : fields) {
            Method m = builderType.getMethod(f.getName(), f.getType());
            assertSame(builder, m.invoke(builder, samples.get(f.getName())), "builder " + f.getName() + " debe encadenar");
        }
        Object viaBuilder = builderType.getMethod("build").invoke(builder);
        assertEquals(bean, viaBuilder);

        // equals / hashCode / toString
        assertEquals(bean, bean);
        assertEquals(bean.hashCode(), viaCtor.hashCode());
        assertNotEquals(null, bean);
        assertNotEquals("otra clase", bean);
        assertNotEquals(bean, type.getDeclaredConstructor().newInstance());
        assertTrue(bean.toString().startsWith(type.getSimpleName() + "("), bean.toString());
        assertDoesNotThrow(() -> type.getDeclaredConstructor().newInstance().toString());
        assertDoesNotThrow(() -> type.getDeclaredConstructor().newInstance().hashCode());
    }

    private static List<Field> instanceFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        for (Field f : type.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()) && !f.isSynthetic()) {
                result.add(f);
            }
        }
        return result;
    }

    private static Class<?> builderClass(Class<?> type) {
        for (Class<?> c : type.getDeclaredClasses()) {
            if (c.getSimpleName().equals("Builder")) {
                return c;
            }
        }
        throw new AssertionError("Sin Builder en " + type.getName());
    }

    private static void invokeSetter(Class<?> type, Object bean, Field f, Object value) throws Exception {
        type.getMethod("set" + capitalize(f.getName()), f.getType()).invoke(bean, value);
    }

    private static Object invokeGetter(Class<?> type, Object bean, Field f) throws Exception {
        String prefix = f.getType() == boolean.class ? "is" : "get";
        return type.getMethod(prefix + capitalize(f.getName())).invoke(bean);
    }

    private static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static Object sampleFor(Class<?> t) {
        if (t == String.class) return "valor";
        if (t == Long.class || t == long.class) return 7L;
        if (t == Integer.class || t == int.class) return 3;
        if (t == Boolean.class || t == boolean.class) return Boolean.TRUE;
        if (t == Double.class || t == double.class) return 1.5d;
        if (t == BigDecimal.class) return new BigDecimal("12.50");
        if (t == LocalDateTime.class) return LocalDateTime.of(2026, 1, 15, 10, 30);
        if (t == LocalDate.class) return LocalDate.of(2026, 1, 15);
        if (t == List.class) return new ArrayList<>();
        if (t.isEnum()) return t.getEnumConstants()[0];
        throw new AssertionError("Tipo sin muestra definida: " + t.getName());
    }
}
