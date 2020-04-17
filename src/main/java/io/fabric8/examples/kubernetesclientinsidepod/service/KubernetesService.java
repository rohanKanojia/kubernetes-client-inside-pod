package io.fabric8.examples.kubernetesclientinsidepod.service;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KubernetesService {
    public KubernetesClient client = null;

    public KubernetesService() {
        this.client = new DefaultKubernetesClient();
    }

    public Map<String, String> getPods() {
        Map<String, String> result = new HashMap<>();
        try {
            PodList podList = client.pods().inNamespace("default").list();
            result.put("message", "There are " + podList.getItems().size() + " pods in default namespace.");
        } catch (KubernetesClientException exception) {
            exception.printStackTrace();
            result.put("error", exception.getMessage());
        } finally {
            return result;
        }
    }
}
