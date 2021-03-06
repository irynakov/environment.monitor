package org.yagel.monitor.config;

import org.yagel.monitor.EnvironmentConfig;
import org.yagel.monitor.MonitorConfig;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "monitorConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonitorConfigImpl implements MonitorConfig {

  private static MonitorConfig config;

  @XmlElement(type = EnvironmentConfigImpl.class)
  private Set<EnvironmentConfig> environments;

  @XmlElement
  private String pluginCollectorLocation;

  @Override
  public Set<EnvironmentConfig> getEnvironments() {
    return environments;
  }


}
