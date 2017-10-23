<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:basePage title="cmas.face.index.header" indexpage="false" doNotDoAuth="true">

    <div class="content" id="Content">
        <div class="header"><span><b><s:message code="cmas.face.client.termsAndCondHeader"/></b></span></div>
        <div class="return">
            <a href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
        <div>
            <div class="question">
                <span><b>By accepting this Policy by online electronic approval you hereby acknowledge and confirm that:</b></span>
                <div class="answer">
                    <ul>
                        <li>
                            CMAS has duly informed you about the collection of your personal data and of the purposes
                            pursuant to the article 13 of D. Lgs. n. 196/2003
                        </li>
                        <li>
                            the processing of your personal data by the Controller, also on behalf of third parties, in
                            order to send informations and/or promotional materials about events, announcements,
                            products or services, also through the use of technological automated instruments and
                            devices (email, sms, fax). You are aware that you can opt-out at any time, contacting the
                            Controller or the Italian Representative
                        </li>
                        <li>
                            the disclosing and/or communication of your personal data (address, age, gender, payment
                            systems, email address, phone numbers, etc.) to third parties (such as other commercial
                            companies) for direct marketing purposes, also through the use of technological automated
                            instruments and devices (email, sms, fax)
                        </li>
                    </ul>
                </div>
            </div>
            <div class="question">
                <span><b>Information pursuant to the art. 13 of the Legislative Decree of 30 June 2003, n. 196</b></span>
            </div>
            <div class="answer">
                According to art. 13 of D. Lgs. n. 196/2003, we hereby inform you that all the information that
                collected herein or in the future (hereinafter “Data”), based on your consent if needed, will be
                collected, recorded and used in compliance with the said Legislative Decree-related provisions. The
                processing of the Data will be carried out in compliance with technical and organizational measures
                providing sufficient guarantees of confidentiality and privacy; such process might also take place
                through the use of technological instruments and devices, which are aimed to collect, register, organize
                and transfer the said data.
            </div>
            <div class="answer">
                In particular, CMAS collects and uses the Data for the following purposes:
                <ol>
                    <li>
                        monitoring and adjourning of personal records and actual technical and sport capabilities,
                        records,
                        levels and achievements of individual members of national federations affiliated with CMAS
                    </li>
                    <li>
                        compliance with laws, codes of conducts, European or European countries norms and to establish
                        or
                        defend a legal claim;
                    </li>
                    <li>
                        technical and sport assistance, registration in tournaments, sport events and championships
                        etc.;
                    </li>
                    <li>
                        with your prior consent, delivery of information and/or promotional materials and commercials
                        about
                        events, announcements, products or services, also through the use of technological automated
                        instruments and devices;
                    </li>
                    <li>
                        sending of products and services-related customer satisfaction surveys for internal statistics
                        and
                        improvement of internal processes;
                    </li>
                    <li>
                        defining the data subject’s profile for marketing purposes, behavioural advertising, evaluation
                        of
                        interests and market trends, products and services development and improvement, selling
                        strategies
                        and improvement of customer services.
                    </li>
                </ol>
            </div>
            <div class="answer">
                The submission of your personal data for the above purposes 1, 2, 3 is required. In the other cases (4,
                5, 6) the submission is optional. Data retention will be strictly limited for the time needed by the
                above purposes or in order to comply with Italian laws and other European provisions of law. Your data
                could be processed by persons in charge of the processing, operating in the marketing, legal,
                commercial, administrative, fiscal, shipping sectors under the direct authority of the data controller.
                Without your prior consent, CMAS will not transfer your Data to third parties outside of CMAS for
                purposes other than those above outlined, unless otherwise required by the laws or by any Governmental
                body. However, Data may occasionally be transferred to third parties who act as Data Processors for or
                on behalf of CMAS in the provision of services and products to customers. When disclosure of Data to
                such third parties is likely or necessary for whatever reason, CMAS endeavours to ensure that such third
                parties provide an equivalent level of protection as CMAS regarding the Data.
            </div>
            <div class="answer">
                CMAS, headquartered in Rome (Italy), Viale Tiziano 72 is the Controller for the Processing of the said
                data. The Data Processor is [name of the processing company]. At any moment, you have the right to
                access the personal information you disclosed and all the rights foreseen under art. 7 of the
                Legislative Decree n. 196/2003, including updating or deletion of records, by contacting the
                aforementioned Controller of the Data Processing or the Data Processor.
            </div>
        </div>
    </div>

</my:basePage>
