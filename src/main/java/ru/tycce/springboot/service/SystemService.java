package ru.tycce.springboot.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.GarbageCollectorMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemService {

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private final OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    private final List<GarbageCollectorMXBean> garbageCollectorMXBean = ManagementFactory.getGarbageCollectorMXBeans()
            .stream()
            .filter(gs->gs instanceof com.sun.management.GarbageCollectorMXBean)
            .map(gs->(GarbageCollectorMXBean)gs)
            .collect(Collectors.toList());
    private final SystemInfo systemInfo = new SystemInfo(operatingSystemMXBean, garbageCollectorMXBean, threadMXBean, memoryMXBean);

    public long hipSize() {return Runtime.getRuntime().totalMemory();}
    public long maxHipSize() {return Runtime.getRuntime().maxMemory();}
    public long freeHipSize() {return Runtime.getRuntime().freeMemory();}
    public double getSystemCPULoadPercent() {return  operatingSystemMXBean.getSystemCpuLoad() * 100.0;}

    @Getter
    private static class SystemInfo {
        private final String name;
        private final String arch;
        private final int processors;
        private final String version;
        private final long totalPhysicalMemorySize;
        private final String gsNameList;
        private final String operatingSystemMXBean;
        private final String threadMXBean;
        private final String memoryMXBean;

        public SystemInfo(OperatingSystemMXBean operatingSystemMXBean,
                          List<GarbageCollectorMXBean> garbageCollectorMXBeanList,
                          ThreadMXBean threadMXBean,
                          MemoryMXBean memoryMXBean) {
            arch = operatingSystemMXBean.getArch();
            processors = operatingSystemMXBean.getAvailableProcessors();
            version = operatingSystemMXBean.getVersion();
            name = operatingSystemMXBean.getName();
            totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
            gsNameList = garbageCollectorMXBeanList.stream().map(MemoryManagerMXBean::getName).collect(Collectors.joining(",","[","]"));
            this.operatingSystemMXBean = operatingSystemMXBean.getClass().getName();
            this.threadMXBean = threadMXBean.getClass().getName();
            this.memoryMXBean = memoryMXBean.getClass().getName();
        }
    }
}
