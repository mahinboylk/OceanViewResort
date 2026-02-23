package com.oceanview.observer;

// Removed: import java.util.ArrayList;
// Removed: import java.util.List;

public interface ReservationSubject {
    void attachObserver(ReservationObserver observer);
    void detachObserver(ReservationObserver observer);
    void notifyObservers();
}