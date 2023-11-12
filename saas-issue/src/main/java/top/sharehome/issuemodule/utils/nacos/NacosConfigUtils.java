package top.sharehome.issuemodule.utils.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.yaml.snakeyaml.Yaml;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import java.util.*;

/**
 * nacos远程配置工具类
 *
 * @author AntonyCheng
 * @since 2023/7/13 09:43:39
 */
public class NacosConfigUtils {
    public NacosConfigUtils() {
    }

    private static final String DATA_ID = "top.sharehome.gateway.user";
    private static final String GROUP = "DEFAULT_GROUP";
    private static final String SERVER_ADDR = "xxx.xxx.xxx.xxx:28848";
    private static final String USERNAME = "nacos";
    private static final String PASSWORD = "admin123456";
    private final Properties properties = new Properties() {
        {
            put("serverAddr", SERVER_ADDR);
            put("username", USERNAME);
            put("password", PASSWORD);
        }
    };

    private ConfigService configService;

    {
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
        }
    }


    public Boolean publishYamlConfig(List<Tenant> tenantList) throws NacosException {
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Object> yamlMap = new LinkedHashMap<String, Object>() {
            {
                put("spring", new LinkedHashMap<String, Object>() {
                    {
                        put("cloud", new LinkedHashMap<String, Object>() {
                            {
                                put("gateway", new LinkedHashMap<String, Object>() {
                                    {
                                        put("routes", new ArrayList<LinkedHashMap<String, Object>>() {
                                            {
                                                tenantList.forEach(tenant -> {
                                                    add(new LinkedHashMap<String, Object>() {
                                                        {
                                                            put("id", tenant.getId());
                                                            put("uri", "http://" + tenant.getIp() + ":" + tenant.getPort());
                                                            put("predicates", new ArrayList<LinkedHashMap<String, Object>>() {
                                                                {
                                                                    add(new LinkedHashMap<String, Object>() {
                                                                        {
                                                                            put("name", "TenantId");
                                                                            put("args", new LinkedHashMap<String, Object>() {
                                                                                {
                                                                                    put("tenantId", new LinkedList<Long>() {
                                                                                        {
                                                                                            add(tenant.getId());
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                    add(new LinkedHashMap<String, Object>() {
                                                                        {
                                                                            put("name", "Path");
                                                                            put("args", new LinkedHashMap<String, Object>() {
                                                                                {
                                                                                    put("patterns", new LinkedList<String>() {
                                                                                        {
                                                                                            add("/ug/api/**");
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
        String content = yaml.dump(yamlMap).replaceAll("\\[", "").replaceAll("]", "");
        return configService.publishConfig(DATA_ID, GROUP, content, "YAML");
    }
}
