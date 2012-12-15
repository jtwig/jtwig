package com.lyncode.jtwig.mvc;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lyncode.jtwig.api.AssetResolver;
import com.lyncode.jtwig.api.ThemeResolver;

public class DefaultAssetResolver implements AssetResolver {
	@Autowired ThemeResolver themeResolver;
	@Autowired HttpServletRequest req;

	private String basePath = "";
	
	
	
	public String getBasePath() {
		return basePath;
	}



	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}



	@Override
	public String getPath(String resource) {
		String path = this.getBasePath();
		if (path == null) path = "";
		if (themeResolver != null) {
			if (themeResolver.getTheme() != null && !themeResolver.getTheme().equals("")) {
				if (!path.endsWith(File.separator))
					path += File.separator;
				path += themeResolver.getTheme();
			}
		}
		if (!path.endsWith(File.separator))
			path += File.separator;
		
		if (resource.startsWith(File.separator))
			resource = resource.substring(1);
		
		path = path + resource;
		if (path.startsWith("/"))
			path = path.substring(1);
		return req.getContextPath() + "/" + path;
	}

}
