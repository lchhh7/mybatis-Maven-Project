/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.example.cmmn.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

public class EgovImgPaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware {

	private ServletContext servletContext;

	public EgovImgPaginationRenderer() {
		// no-op
	}

	/** PaginationRenderer.
	*
	* @see 개발프레임웍크 실행환경 개발팀
	*/
	public void initVariables() {
		
		/*
		firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">"
					   + "<image src='" + servletContext.getContextPath()
					   + "/images/egovframework/cmmn/btn_page_pre10.gif' border=0/></a>&#160;";
		previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">"
						  + "<image src='" + servletContext.getContextPath()
						  + "/images/egovframework/cmmn/btn_page_pre1.gif' border=0/></a>&#160;";
		currentPageLabel = "<strong>{0}</strong>&#160;";
		otherPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
		nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">"
					  + "<image src='" + servletContext.getContextPath()
					  + "/images/egovframework/cmmn/btn_page_next1.gif' border=0/></a>&#160;";
		lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">"
					  + "<image src='" + servletContext.getContextPath()
					  + "/images/egovframework/cmmn/btn_page_next10.gif' border=0/></a>&#160;";
		*/
		firstPageLabel = "<div><a href=\"#\" onclick=\"{0}({1}); return false;\"><img src=\"" + servletContext.getContextPath() + "/common/main/img/first.png\" alt=\"처음\"></a></div>";
		previousPageLabel = "<div><a href=\"#\" onclick=\"{0}({1}); return false;\"><img src=\"" + servletContext.getContextPath() + "/common/main/img/previous.png\" alt=\"이전\"></a></div>";
		currentPageLabel = "<div><a href=\"#\" class=\"num acTive\">{0}</a></div>";
		otherPageLabel = "<div><a href=\"#\" onclick=\"{0}({1}); return false;\" class=\"num\">{2}</a></div>";
		nextPageLabel = "<div><a href=\"#\" onclick=\"{0}({1}); return false;\"><img src=\"" + servletContext.getContextPath() + "/common/main/img/next.png\" alt=\"다음\"></a></div>";
		lastPageLabel = "<div><a href=\"#\" onclick=\"{0}({1}); return false;\"><img src=\"" + servletContext.getContextPath() + "/common/main/img/end.png\" alt=\"마지막\"></a></div>";
		
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}
}
