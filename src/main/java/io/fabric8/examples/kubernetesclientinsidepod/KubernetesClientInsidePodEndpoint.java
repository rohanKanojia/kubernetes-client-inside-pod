package io.fabric8.examples.kubernetesclientinsidepod;

import io.fabric8.examples.kubernetesclientinsidepod.service.KubernetesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KubernetesClientInsidePodEndpoint {
    @RequestMapping(value = "/", produces = "application/json")
    public Map<String, String> getPods() {
        KubernetesService service = new KubernetesService();
        return service.getPods();
    }
}
