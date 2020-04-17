# Kubernetes Client Inside A Pod

This is a demo application which demonstrates use of [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client). It just tries to list number of pods in desired namespace.

## Make sure to give the default service account cluster-admin privileges:
Example for `ServiceAccount` named `default` in `default` namespace:
```
kubectl create clusterrolebinding default-pod --clusterrole cluster-admin --serviceaccount=default:default
```

## Deploy Application to Kubernetes Using [Eclipse JKube](https://github.com/eclipse/jkube)
```
mvn package k8s:build k8s:resource k8s:apply
```
<img src="https://i.imgur.com/HExlqoL.png" />

### Access Service
<img src="https://i.imgur.com/48TIAaL.png" />
