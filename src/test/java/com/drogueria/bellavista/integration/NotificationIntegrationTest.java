package com.drogueria.bellavista.integration;

import com.drogueria.bellavista.application.service.NotificationApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Notification system.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationApplicationService notificationService;

    @Test
    @WithMockUser(roles = "USER")
    public void testGetNotificationsForUserRole() throws Exception {
        mockMvc.perform(get("/api/notifications")
                .param("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetUnreadNotifications() throws Exception {
        mockMvc.perform(get("/api/notifications/unread")
                .param("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllNotificationsAsAdmin() throws Exception {
        mockMvc.perform(get("/api/notifications/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testMarkNotificationAsRead() throws Exception {
        // This test assumes a notification exists. In a real scenario,
        // you would first create a notification or use @Sql to insert test data
        mockMvc.perform(put("/api/notifications/999/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect 404 for non-existent notification
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGenerateInventoryAlerts() throws Exception {
        mockMvc.perform(post("/api/notifications/generate-inventory-alerts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteNotification() throws Exception {
        // This test assumes a notification exists. In a real scenario,
        // you would first create a notification or use @Sql to insert test data
        mockMvc.perform(delete("/api/notifications/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect 404 for non-existent notification
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAccessAdminEndpointAsUser() throws Exception {
        mockMvc.perform(get("/api/notifications/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAccessAdminGenerateAlertsAsUser() throws Exception {
        mockMvc.perform(post("/api/notifications/generate-inventory-alerts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    public void testCreateAndMarkNotificationAsRead() throws Exception {
        // First generate some inventory alerts to potentially create notifications
        notificationService.generateInventoryAlerts();

        // Get notifications to check if any were created
        String response = mockMvc.perform(get("/api/notifications/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // If notifications were created, try to mark one as read
        // Otherwise, expect 404 when trying to mark a non-existent notification
        if (response.contains("\"id\":1")) {
            // Notification with ID 1 exists, should be able to mark as read
            mockMvc.perform(put("/api/notifications/1/read")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } else {
            // No notification with ID 1, expect 404
            mockMvc.perform(put("/api/notifications/1/read")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testNotificationsEndpointReturnsValidJson() throws Exception {
        mockMvc.perform(get("/api/notifications")
                .param("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUnreadNotificationsEndpointReturnsValidJson() throws Exception {
        mockMvc.perform(get("/api/notifications/unread")
                .param("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}