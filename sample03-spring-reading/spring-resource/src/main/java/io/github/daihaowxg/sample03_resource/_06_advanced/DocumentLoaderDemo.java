package io.github.daihaowxg.sample03_resource._06_advanced;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * {@link org.springframework.beans.factory.xml.DocumentLoader} 示例
 * 展示如何编程式地使用 DefaultDocumentLoader 解析 Spring XML 配置文件
 */
public class DocumentLoaderDemo {

    public static void main(String[] args) {
        try {
            // 1. 定位资源文件
            Resource resource = new ClassPathResource("sample-beans.xml");
            System.out.println(">>> 正在加载资源: " + resource.getFilename());

            // 2. 创建 DefaultDocumentLoader (Spring 内部解析 XML 的核心策略实现)
            DefaultDocumentLoader documentLoader = new DefaultDocumentLoader();

            // 3. 执行文档加载
            // 参数说明：
            // - inputSource: 资源的输入源
            // - entityResolver: 用于解析外部实体 (如 DTD/XSD)，使用 DefaultHandler 提供空实现
            // - errorHandler: 异常处理器，使用 DefaultHandler 提供空实现
            // - validationMode: 验证模式 (0 表示自动探测)
            // - namespaceAware: 是否支持命名空间 (解析 Spring 标签通常设为 true)
            DefaultHandler handler = new DefaultHandler();
            Document document = documentLoader.loadDocument(
                    new InputSource(resource.getInputStream()),
                    handler,
                    handler,
                    0,
                    true);

            // 4. 解析结果展示
            Element root = document.getDocumentElement();
            System.out.println(">>> 根元素名称: " + root.getNodeName());
            System.out.println(">>> 命名空间: " + root.getAttribute("xmlns"));

            // 获取所有 bean 节点
            NodeList beans = root.getElementsByTagName("bean");
            for (int i = 0; i < beans.getLength(); i++) {
                Element bean = (Element) beans.item(i);
                System.out.printf(">>> 发现 Bean: ID=%s, Class=%s%n",
                        bean.getAttribute("id"),
                        bean.getAttribute("class"));
            }

        } catch (Exception e) {
            System.err.println("解析失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
