package com.qiguliuxing.dts.wx.config;

import java.io.File;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qiguliuxing.dts.wx.annotation.support.LoginUserHandlerMethodArgumentResolver;

@Configuration
public class WxWebMvcConfiguration implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String rootPath = new File("").getAbsolutePath();
		String pathString = rootPath.toString();
		pathString = pathString.replace("\\", "/");
		registry.addResourceHandler("/storage/**").addResourceLocations("file:"+pathString+"/dts/storage/");
	}
}
