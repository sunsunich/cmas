package org.cmas.util.presentation.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomExceptionResolver extends SimpleMappingExceptionResolver {

    private final Logger log = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private JsonViewFactory jsonFactory;
//    @Autowired
//    private DaemonStatisticService statisticService;

    @Override
    public void setDefaultErrorView(String defaultErrorView) {
        throw new IllegalArgumentException();
    }

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        if (determineViewName(ex, request) == null) {
            log.error(buildLogMessage(ex, request), ex);
        }
    }

    /*
     * Надо в Ajax запрос (его можно отличить по HTTP заголовку X-Requested-With: XMLHttpRequest)
     * выдавать {success:false, message:'текст_из_message_от_эксцепшна'}, а в обычном запросе выдавать
     * специальную страничку в нормальном дизайне нашего сайта с текстом ошибки, если он есть.
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = super.resolveException(request,response,handler,ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String xRequestWithHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestWithHeader)) {
            if (mv == null) {
                mv = new ModelAndView();
            }
            final String message = ex.getMessage();
//            mv.setView(
//                    jsonFactory.createSelfRenderedView(new JSONRender() {
//                        @Override
//                        public void buildJSON(JSONBuilder builder) {
//                            builder.key("success").value(false);
//                            builder.key("message").value(message);
//                        }
//                    })
//            );
        } else {
            if (mv == null) {
                mv = new ModelAndView();
                mv.addObject("exception", ex);
            }
            mv.setViewName("/error");
        }
        return mv;
	}

}

