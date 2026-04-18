package com.crm.alert.controller;

<<<<<<< Updated upstream
import com.crm.alert.factory.AlertFactory;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public class AlertController {
    private final AlertRepository alertRepository;
=======
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public abstract class AlertController {
    protected final AlertRepository alertRepository;
>>>>>>> Stashed changes

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

<<<<<<< Updated upstream
    public SystemAlert createAlert(AlertFactory factory, String alertId, String referenceId) {
        SystemAlert alert = factory.createAlert(alertId, referenceId);
        alertRepository.save(alert);
=======
    // This is the Abstract Factory Method
    protected abstract SystemAlert createAlert(String id, String ref);

    // This is the operation that uses the factory method
    public SystemAlert processAlert(String id, String ref) {
        SystemAlert alert = createAlert(id, ref);
        alertRepository.save(id, alert);
>>>>>>> Stashed changes
        return alert;
    }

    public void resolveAlert(String alertId) {
        SystemAlert a = alertRepository.findById(alertId);
        if (a != null) {
            a.resolve();
<<<<<<< Updated upstream
            alertRepository.update(a);
=======
            alertRepository.update(alertId, a);
>>>>>>> Stashed changes
        }
    }
}
