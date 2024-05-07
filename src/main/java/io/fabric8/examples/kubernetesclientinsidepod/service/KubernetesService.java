package io.fabric8.examples.kubernetesclientinsidepod.service;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class KubernetesService {
    public static KubernetesClient client;

    public KubernetesService() {
        this.client = new KubernetesClientBuilder().build();
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

    public Map<String, String> getDeploymentName() {
        File hostName = new File("/etc/hostname");
        try {
            String podName = new String(Files.readAllBytes(hostName.toPath()));
            Pod pod = client.pods().inNamespace("default").withName(podName).get();
            OwnerReference replicaSetOwnerRef = getControllerOwnerReference(pod);
            if (replicaSetOwnerRef != null) {
                ReplicaSet replicaSet = client.apps().replicaSets().inNamespace("default").withName(replicaSetOwnerRef.getName()).get();
                OwnerReference deploymentOwnerRef = getControllerOwnerReference(replicaSet);
                if (deploymentOwnerRef != null) {
                    Deployment deployment = client.apps().deployments().inNamespace("default").withName(deploymentOwnerRef.getName()).get();
                    return Collections.singletonMap("name", deployment.getMetadata().getName());
                }
            }
        } catch (IOException ioException) {
            return Collections.singletonMap("error", ioException.getMessage());
        }
        return Collections.emptyMap();
    }

    private OwnerReference getControllerOwnerReference(HasMetadata resource) {
        return resource.getMetadata().getOwnerReferences().stream()
            .filter(o -> Boolean.TRUE.equals(o.getController()))
            .findAny()
            .orElse(null);
    }
}
