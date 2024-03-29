package util;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsopUtil {
	
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
	private JsopUtil() {};
	
	
	public static Document jsopInit(String targetUrl) throws IOException {
	      Document document = Jsoup.connect(targetUrl).userAgent(userAgent).get();
	      return document;
	}
}
