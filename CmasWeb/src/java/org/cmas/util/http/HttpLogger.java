package org.cmas.util.http;

import org.cmas.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpLogger {

    private static final Logger logger = LoggerFactory.getLogger(HttpLogger.class);

    public static void logHttp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws
            IOException {


        long startTime = System.currentTimeMillis();

        Map<String, String> requestMap = getTypesafeRequestMap(httpServletRequest);

        BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);

        final String logMessage = new StringBuilder("REST Request - ")

                .append("[HTTP METHOD:")

                .append(httpServletRequest.getMethod())

                .append("] [PATH INFO:")

                .append(httpServletRequest.getPathInfo())

                .append("] [REQUEST PARAMETERS:")

                .append(requestMap)
//                .append("] [REQUEST BODY:")
//
//                .append(bufferedReqest.getRequestBody())

                .append("] [REMOTE ADDRESS:")

                .append(httpServletRequest.getRemoteAddr())

                .append(']')

                .toString();

        logger.info(logMessage + " - is being processed... ");

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;

        logger.info(logMessage + " - processed succesfully in " + totalTime + "ms");

    }


    private static Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {

        Map<String, String> typesafeRequestMap = new HashMap<String, String>();


        Enumeration<?> requestParamNames = request.getParameterNames();

        while (requestParamNames.hasMoreElements()) {

            String requestParamName = (String) requestParamNames.nextElement();

            String requestParamValue = request.getParameter(requestParamName);

            typesafeRequestMap.put(requestParamName, requestParamValue);

        }

        return typesafeRequestMap;

    }

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {


        private ByteArrayInputStream bais = null;

        private ByteArrayOutputStream baos = null;

        private BufferedServletInputStream bsis = null;

        private byte[] buffer = null;


        public BufferedRequestWrapper(HttpServletRequest req)

                throws IOException {

            super(req);

            // Read InputStream and store its content in a buffer.

            InputStream is = req.getInputStream();

            this.baos = new ByteArrayOutputStream();

            byte buf[] = new byte[1024];

            int letti;

            while ((letti = is.read(buf)) > 0)

            {

                this.baos.write(buf, 0, letti);

            }

            this.buffer = this.baos.toByteArray();

        }


        @Override
        public ServletInputStream getInputStream()

        {

            // Generate a new InputStream by stored buffer

            this.bais = new ByteArrayInputStream(this.buffer);

            // Istantiate a subclass of ServletInputStream

            // (Only ServletInputStream or subclasses of it are accepted by

            // the servlet engine!)

            this.bsis = new BufferedServletInputStream(this.bais);

            return this.bsis;

        }


        String getRequestBody() throws IOException

        {

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));

            String line = null;

            StringBuilder inputBuffer = new StringBuilder();

            do {

                line = reader.readLine();

                if (null != line) {

                    inputBuffer.append(StringUtil.correctSpaceCharAndTrim(line));

                }

            } while (line != null);

            reader.close();

            return StringUtil.correctSpaceCharAndTrim(inputBuffer.toString());

        }

    }


    /*

    * Subclass of ServletInputStream needed by the servlet engine. All

    * inputStream methods are wrapped and are delegated to the

    * ByteArrayInputStream (obtained as constructor parameter)!

    */

    private static final class BufferedServletInputStream extends ServletInputStream {


        private ByteArrayInputStream bais;


        public BufferedServletInputStream(ByteArrayInputStream bais) {

            this.bais = bais;

        }


        @Override
        public int available() {

            return this.bais.available();

        }


        @Override
        public int read() {

            return this.bais.read();

        }


        @Override
        public int read(byte[] buf, int off, int len) {

            return this.bais.read(buf, off, len);

        }


    }


}
