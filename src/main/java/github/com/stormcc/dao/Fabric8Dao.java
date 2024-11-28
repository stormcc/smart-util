package github.com.stormcc.dao;

/**
 * Create By: Jimmy Song
 * Create At: 2024-11-12 14:27
 */

import github.com.stormcc.util.JacksonUtil;
import io.fabric8.kubernetes.api.model.LoadBalancerIngress;
import io.fabric8.kubernetes.api.model.LoadBalancerStatus;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.networking.v1beta1.Ingress;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class Fabric8Dao {
    private static final String NAME_SPACE = "default";
    private static final int TAILING_LINE_NUMBER = 100;
//    private static final String MASTER_ADDR="https://127.0.0.1:8443/";
//    private static final String MASTER_ADDR="https://127.0.0.1:59467/";
    private static final String MASTER_ADDR="http://127.0.0.1:59467/";


    public static KubernetesClient createClient() {
        log.info("master address is:{}", MASTER_ADDR);
        Config config = new ConfigBuilder().withMasterUrl(MASTER_ADDR).withNamespace(NAME_SPACE).build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        log.info("create KubernetesClient success with sa");
        return client;
    }

    public static void main(String[] args) {
//        node();
//        tailPodLog1(10);
        tailPodLogOnService("kubernetes-dashboard", 10);
    }

    private static void node() {
        log.info("start...");
        try (KubernetesClient client = createClient()) {
            // 获取集群中的节点列表
            NodeList nodeList = client.nodes().list();

            long totalMemory = 0;
            long usedMemory = 0;
            int totalCpuCores = 0;
            int allocatedCpuCores = 0;
            int totalGpuCores = 0;
            int allocatedGpuCores = 0;

            // 遍历节点列表，累加每个节点的资源信息
            for (Node node : nodeList.getItems()) {
                Map<String, Quantity> capacity = node.getStatus().getCapacity();
//                ResourceList capacity = capacity1
                Map<String, Quantity> allocatable = node.getStatus().getAllocatable();
//                ResourceList allocatable = allocatable1
                // 处理内存信息
                String memoryCapacityStr = capacity.get("memory").toString();
                String memoryUsedStr = allocatable.get("memory").toString();

                totalMemory += parseMemoryValue(memoryCapacityStr);
                usedMemory += parseMemoryValue(memoryUsedStr);

                // 处理CPU信息
                String cpuCapacityStr = capacity.get("cpu").toString();
                String cpuAllocatedStr = allocatable.get("cpu").toString();

                totalCpuCores += parseCpuValue(cpuCapacityStr);
                allocatedCpuCores += parseCpuValue(cpuAllocatedStr);

                // 处理GPU信息（假设GPU资源以 "nvidia.com/gpu" 形式存在，可根据实际情况调整）
                if (capacity.containsKey("nvidia.com/gpu")) {
                    String gpuCapacityStr = capacity.get("nvidia.com/gpu").toString();
                    String gpuAllocatedStr = allocatable.get("nvidia.com/gpu").toString();

                    totalGpuCores += Integer.parseInt(gpuCapacityStr);
                    allocatedGpuCores += Integer.parseInt(gpuAllocatedStr);
                }

            }
            client.pods().watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {

                }

                @Override
                public void onClose(WatcherException e) {

                }
            });
            PodList podList = client.pods().inNamespace("dscloud").list();
            for (Pod pod : podList.getItems()) {
            }

            List<Ingress> ingressList = client.network().ingresses().list().getItems();
            for (Ingress ingress : ingressList) {
            }

            List<Node> nodes = client.nodes().list().getItems();

            System.out.println("Kubernetes集群总内存: " + formatMemoryValue(totalMemory));
            System.out.println("Kubernetes集群已使用内存: " + formatMemoryValue(usedMemory));
            System.out.println("Kubernetes集群CPU总核数: " + totalCpuCores);
            System.out.println("Kubernetes集群CPU已分配核数: " + allocatedCpuCores);
            System.out.println("Kubernetes集群GPU总核数: " + totalGpuCores);
            System.out.println("Kubernetes集群GPU已分配核数: " + allocatedGpuCores);
        }
    }

    private static void tailPodLogOnService(String serviceName, int lineNumber){
        try (KubernetesClient client = createClient()) {
            String nameSpace = "kubernetes-dashboard";
            String labelName = "k8s-app";
            log.warn("nameSpace is:{}, labelName is:{}, serviceName is:{}", nameSpace, labelName, serviceName);
            Service service = client.services().inNamespace(nameSpace).withName(serviceName).get();
            String labelValue = service.getSpec().getSelector().get(labelName);
            log.info("labelValue is:{}", labelValue);
            List<Pod> podList = client.pods().inNamespace(nameSpace).withLabel(labelName+"="+labelValue).list().getItems();
            log.info("podList number is:{}", podList.size());
            for (Pod pod : podList) {
                String podName = pod.getMetadata().getName();
                log.warn("podName is:{}", podName);
                String prettyLog = client.pods().inNamespace(nameSpace).withName(podName).getLog(true);
                String[] lines = prettyLog.split("\n");
                List<String> strings = tailLines(lines, lineNumber);
                for (String line : strings) {
                    log.warn("podName is:{}, log is:{}", podName, line);
                }
            }
        }
    }


    private static void tailPodLog(int lineNumber){
        try (KubernetesClient client = createClient()) {
            String nameSpace = "kubernetes-dashboard";
            PodList podList = client.pods().inNamespace(nameSpace).list();
            List<Pod> pods = podList.getItems();
            for (Pod pod : pods) {
                String podName = pod.getMetadata().getName();
                log.warn("podName is:{}", podName);
                String prettyLog = client.pods().inNamespace(nameSpace).withName(podName).getLog(true);
                String[] lines = prettyLog.split("\n");
                List<String> strings = tailLines(lines, lineNumber);
                for (String line : strings) {
                    log.warn("podName is:{}, log is:{}", podName, line);
                }
            }
        }
    }

    private static List<String> tailLines(String[] lines, int lineNumber) {
        if (lines == null || lines.length == 0) {
            return new ArrayList<>(0);
        }
        if (lineNumber >= lines.length) {
            List<String> list = new ArrayList<>(lines.length);
            Collections.addAll(list, lines);
            return list;
        }
        List<String> list = new ArrayList<>(lineNumber);
        int start = lines.length - lineNumber;
        list.addAll(Arrays.asList(lines).subList(start, lines.length));
        return list;
    }

    private static void tailPodLog3(){
        try (KubernetesClient client = createClient()) {
            String nameSpace = "kubernetes-dashboard";
            PodList podList = client.pods().inNamespace(nameSpace).list();
            List<Pod> pods = podList.getItems();
            for (Pod pod : pods) {
                String podName = pod.getMetadata().getName();
                log.warn("podName is:{}", podName);
                Reader logReader = client.pods().inNamespace(nameSpace).withName(podName).getLogReader();
                StringWriter stringWriter = new StringWriter();
                IOUtils.copy(logReader, stringWriter);
                log.warn("podName is:{}, log is:{}", podName, stringWriter);
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException.", e);
        }
    }

    private static void tailPodLog2(){
        try (KubernetesClient client = createClient()) {
            String nameSpace = "kube-system";
            PodList podList = client.pods().inNamespace(nameSpace).list();
            List<Pod> pods = podList.getItems();
            for (Pod pod : pods) {
                String podName = pod.getMetadata().getName();
                log.warn("podName is:{}", podName);
//                LogWatch logWatch = client.pods().inNamespace(nameSpace).withName(podName).tailingLines(10).watchLog();
//                logWatch.wait(10000);
                Reader logReader = client.pods().withName(podName).getLogReader();
                StringWriter stringWriter = new StringWriter();
                IOUtils.copy(logReader, stringWriter);
                log.warn("podName is:{}, log is:{}", podName, stringWriter);
//                InputStream inputStream = logWatch.getOutput();
//                StringWriter writer = new StringWriter();
//                String encoding = StandardCharsets.UTF_8.name();
//                IOUtils.copy(inputStream, writer, encoding);
//                log.info("writer is:{}", writer.toString());
            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException("IOException.", e);
        }
    }


    private static void tailPodLog0(HttpServletResponse response){
        String podName = "your-pod-name";
        String namespace = "your-namespace";

        try (KubernetesClient client = createClient()) {
            LogWatch logWatch = client.pods().inNamespace(NAME_SPACE).withName(podName).tailingLines(10).watchLog();
            logWatch.wait(10000, 100);
            ServletOutputStream os = response.getOutputStream();
            InputStream inputStream = logWatch.getOutput();
            byte[] bytes = new byte[1024];
            int readLength;
            while (( readLength = inputStream.read(bytes)) != -1) {
                os.write(bytes, 0 , readLength);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException("IOException.", e);
        }
    }

    private static String servicePodName(String serviceName){
        try (KubernetesClient client = createClient()) {
            Service service = client.services().inNamespace(NAME_SPACE).withName(serviceName).get();
//            service.getStatus().getLoadBalancer().
        }
        return null;
    }

    private static void ingressTest(){
        try (KubernetesClient client = createClient()) {
            List<Ingress> list = client.network().ingress().inNamespace("default").withLabel("labelName", "labelValue").list().getItems();
            for (Ingress ingress : list) {
                List<LoadBalancerIngress> ingressList = ingress.getStatus().getLoadBalancer().getIngress();
                for (LoadBalancerIngress loadBalancerIngress : ingressList) {
                    log.info("getAdditionalProperties is:{}", JacksonUtil.valueAsString(loadBalancerIngress.getAdditionalProperties()));
                }
            }
            client.network().ingresses().watch(new Watcher<Ingress>() {
                @Override
                public void eventReceived(Action action, Ingress resource) {
                    resource.getStatus();
                }

                @Override
                public void onClose(WatcherException cause) {

                }
            });

            Ingress ingress1 = client.network().ingresses().inNamespace(NAME_SPACE).withName("ingressName").get();
            LoadBalancerStatus loadBalancer = ingress1.getStatus().getLoadBalancer();

            List<Ingress> ingressList = client.network().ingresses().inNamespace(NAME_SPACE).list().getItems();
            for (Ingress ingress : ingressList) {
                ingress.getStatus();
            }
        }
    }
    private static void ingress(){
        try (KubernetesClient client = createClient()) {
            client.network().ingresses().watch(new Watcher<Ingress>() {
                @Override
                public void eventReceived(Action action, Ingress resource) {
                    resource.getStatus();
                }

                @Override
                public void onClose(WatcherException cause) {

                }
            });

            Ingress ingress1 = client.network().ingresses().inNamespace(NAME_SPACE).withName("ingressName").get();
            LoadBalancerStatus loadBalancer = ingress1.getStatus().getLoadBalancer();

            List<Ingress> ingressList = client.network().ingresses().inNamespace(NAME_SPACE).list().getItems();
            for (Ingress ingress : ingressList) {
                ingress.getStatus();
            }
        }
    }

    private static long parseMemoryValue(String memoryValueStr) {
        // 内存值的格式通常是类似 "16Gi"，这里将其转换为字节数
        if (memoryValueStr.endsWith("Ki")) {
            return Long.parseLong(memoryValueStr.substring(0, memoryValueStr.length() - 2)) * 1024;
        } else if (memoryValueStr.endsWith("Mi")) {
            return Long.parseLong(memoryValueStr.substring(0, memoryValueStr.length() - 2)) * 1024 * 1024;
        } else if (memoryValueStr.endsWith("Gi")) {
            return Long.parseLong(memoryValueStr.substring(0, memoryValueStr.length() - 2)) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(memoryValueStr);
        }
    }

    private static int parseCpuValue(String cpuValueStr) {
        // CPU值的格式可能是类似 "2" 或 "2000m"（表示2个核或2000毫核），这里统一转换为核数
        if (cpuValueStr.endsWith("m")) {
            return Integer.parseInt(cpuValueStr.substring(0, cpuValueStr.length() - 1)) / 1000;
        } else {
            return Integer.parseInt(cpuValueStr);
        }
    }

    private static String formatMemoryValue(long memoryValue) {
        if (memoryValue >= 1024 * 1024 * 1024) {
            return (memoryValue / (1024 * 1024 * 1024)) + "Gi";
        } else if (memoryValue >= 1024 * 1024) {
            return (memoryValue / (1024 * 1024)) + "Mi";
        } else if (memoryValue >= 1024) {
            return (memoryValue / 1024) + "Ki";
        } else {
            return memoryValue + "B";
        }
    }
}