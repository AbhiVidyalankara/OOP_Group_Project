package service;

import model.Notification;
import storage.FileStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private static List<Notification> notifications = new ArrayList<>();
    private static int nextId = 1;

    static {
        notifications = FileStorage.loadNotifications();
        if (!notifications.isEmpty()) {
            nextId = notifications.stream().mapToInt(Notification::getId).max().orElse(0) + 1;
        }
    }

    public static void addNotification(String type, String recipient, String message) {
        Notification notification = new Notification(nextId++, type, recipient, message, LocalDateTime.now());
        notifications.add(notification);
        FileStorage.saveNotifications(notifications);
    }

    public static List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public static List<Notification> getUnreadNotifications() {
        List<Notification> unread = new ArrayList<>();
        for (Notification n : notifications) {
            if (!n.isRead()) {
                unread.add(n);
            }
        }
        return unread;
    }

    public static void markAsRead(int notificationId) {
        for (Notification notification : notifications) {
            if (notification.getId() == notificationId) {
                notification.setRead(true);
                FileStorage.saveNotifications(notifications);
                break;
            }
        }
    }

    public static void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        FileStorage.saveNotifications(notifications);
    }
}