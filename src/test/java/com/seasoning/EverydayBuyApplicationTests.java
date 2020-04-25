package com.seasoning;

import com.alibaba.fastjson.JSONObject;
import com.seasoning.util.CommonUtil;
import com.seasoning.util.WechatApiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EverydayBuyApplicationTests {

	@Autowired
	private WechatApiUtil wechatApiUtil;
	@Test
	public void contextLoads() {
		String result = wechatApiUtil.placeOrder(1222341211, "15230");
		System.out.println(result);

		Map<String, String> map = CommonUtil.getXmlBodyContext(result);

		System.out.println(JSONObject.toJSONString(map));
//		try {
//            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			StringReader sr = new StringReader(result);
//			InputSource is = new InputSource(sr);
//			Document document = documentBuilder.parse(is);
//			document.getDocumentElement().normalize();
//			System.out.println(document.getFirstChild().getNodeValue());
//			NodeList nodeList = document.getChildNodes();
//			System.out.println(document.getElementsByTagName("return_code").item(1).getNodeValue());
//			System.out.println(document.getElementsByTagName("return_msg").item(0).getNodeValue());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	}

}
