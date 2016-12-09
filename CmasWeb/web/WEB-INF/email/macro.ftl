<#-- email send template -->
<#macro mail title="" automail=true>
    <div style="background: #f7f7f7; padding:20px 0">
    <div style="margin: 0 auto; width: 800px; background: #ffffff;">
    <div style="font-size: 24px; color:#3a3a3a;margin-top: 20px;text-align:center; width:800px;">${title}</div>  
    <div style= "border-top: 4px solid #e55e04; width: 800px; margin-top: 10px; margin-bottom: 10px;"></div>
    <div style=" width: 700px; margin: 0 auto;">
    
        <#nested>
    <#if automail>    
    <p>This email has been sent automatically </p>
    </#if>
    <br/>   
    </div>
    <div style= "border-top: 4px solid #e55e04; width: 800px; margin-top: 10px;"></div>
    </div>
    </div>
    </div>
    
</#macro>
    
<#-- Ссылка -->
<#macro href url value="Link" word=false>
<a href="${url}" style="color: #167a9b;"><#if word>${value}<#else>${url}</#if></a>
</#macro>
