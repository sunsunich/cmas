<#import "macro.ftl" as mailer/>
<@mailer.mail title="CMAS AQUALINK: verification of divers certificates">
    <p>Dear ${federation.name},</p>
    <p>
        Hope this email finds you well.
    </p>
    <p>
        We are writing you following the CMAS Head Quarters request to engage your cooperation with CMAS AquaLink – a
        web service and mobile applications for divers:<br/>
        <@mailer.href url="${context_path}"/><br/>
        https://apps.apple.com/app/id1482247868<br/>
    </p>
    <p>
        CMAS AquaLink is a global information system developed by CMAS in order to provide divers with secure and fast
        certificates verification worldwide and improve connectivity and communication within divers community.
        Most importantly, CMAS would like all it's divers' certificates to be verified online 24/7.
    </p>
    <p>
        To achieve this goal we require assistance from every National Federation which is a member of CMAS. At the moment
        National Federations issue only hard copies of certificates – CMAS cards. Now CMAS Head Quarters has made a
        decision to issue secure CMAS e-cards, which could be neither lost, nor faked. CMAS AquaLink is a tool to issue
        and verify such CMAS e-cards.<br/>
        In order to use this tool you are given access to the private section of the web site (the portal) and an admin
        account dedicated to your federation. The details on how to login to the portal are given below in this
        email.
    </p>
    <p>
        There are 2 ways how CMAS e-cards could be issued:
    </p>
    <ol>
        <li>
            Diver registers at CMAS AquaLink and requests certificate approval from Federation<br/>
            For each diver's request CMAS AquaLink will send an email to<br/>
            ${federationAdmin.email}<br/>
            The email will contain the instructions on how to review and accept or decline the diver's request.<br/>
        </li>
        <li>
            Alternatively, Federation adds a diver to CMAS AquaLink before diver registers<br/>
            In this case a responsible person in your Federation should upload divers' details using the CMAS AquaLink
            federation admin portal. After that divers registering at CMAS AquaLink would be able to use their
            certificates numbers or personal details to locate themselves in CMAS AquaLink database. Once located in the
            database divers will be registered as verified CMAS divers straight away, without any additional certificate
            approval requests.<br/>
            This framework is much more convenient for the divers, therefore, we encourage you to upload divers' details
            in advance.<br/>
            This email contains instructions on how to upload divers' data. Please find it at the end.
        </li>
    </ol>
    <p>
        To achieve our common goal of online divers' certificates verification, the following action from a diver is
        required: register at CMAS AquaLink.<br/>
        The required action from your Federation would be: either upload diver's data in advance, or approve diver's
        certificate approval request.<br/>
    </p>
    <p>
        Please find below technical details on CMAS AquaLink federation admin portal.
    </p>
    <p>
        <b>How to login to CMAS AquaLink federation admin portal</b><br/>
        Please login here:<br/>
        <@mailer.href url="${context_path}/login-form.html"/><br/>
        E-mail:<br/>
        ${federationAdmin.email}<br/>
        If you login at CMAS AquaLink for the first time, your password is:<br/>
        cmasdata<br/>
        <br/>
        If you have already used CMAS AquaLink before, please use the password you had setup.<br/>
        If you do not remember your password, please use the following form to recover it:<br>
        <@mailer.href url="${context_path}/lostPasswordForm.html"/><br/>
    </p>
    <p>
        After the first successful login please change your password.<br/>
        Please find attached file: <b>changePassword.png</b>. It shows a link to the change password page.<br/>
        Open the link. Enter your current password "cmasdata" in "Current password" field.<br/>
        Type your new password in the "New password" field.<br/>
        Type your new password again in the "Repeat password" field. Press the "Confirm" button.
    </p>
    <p>
        <b>How to review divers' certificate approval requests</b><br/>
        Please find the attached file: <b>certificateApprovalRequests.png</b>. It shows a link to the page, where you
        can
        review, approve and decline divers' certificate approval requests.<br/>
        On that page you will see a list of requests. Each item has the “View request” button. Please press this button
        to review and approve or decline the request.<br/>
    </p>
    <p>
        <b>How to upload divers' data</b><br/>
        At the moment we developed mechanisms to upload the divers' data and certificates using excel files or manually
        one by one.<br/>
    </p>
    <ol>
        <li>
            Add divers of your federation one by one<br/>
            Please find the attached file: <b>addSingleDiver.png</b>. It shows a link to the page, where you can add a
            single
            diver.<br/>
            Follow the instructions on that page to add a new diver
        </li>
        <li>
            Upload divers of your federations using excel file<br/>
            This is the way to add many divers at one go.<br/>
            Please find attached file: <b>uploadDivers.png</b>. It shows control buttons to upload an excel file.<br/>
            To ensure that all the information is saved correctly in the system, please follow the uniform format for
            the excel file.<br/>
            The format and the example excel file can be found attached:<br/>
            <b>FederationDataExchangeFormat.doc</b> - document describing the way divers' data should be organised
            in excel files.<br/>
            <b>CMAS_Technical_Certificates_to_AquaLink_mapping.doc</b> and <b>CMAS_Scientific_Certificates_to_AquaLink_mapping.doc</b>
            - documents describing the way CMAS Certificates are mapped into the AquaLink System.<br/>
            <b>Data_Example1.xls</b> - an example excel file, containing data for one diver.
        </li>
    </ol>
    <p>
        If you find excel files data exchange format too complicated we are happy to receive your feedback in order to
        improve the data exchange process.<br/>
        Please provide us with the information on how divers' data is stored in your federation and we will be happy to
        automate the data exchange process.<br/>
        Please see <b>FederationDataExchangeOptions.doc</b> attached for more information.
    </p>
    <p>
        <b>How to edit divers data</b><br/>
        You can edit divers' data through the admin portal. Please note that you cannot edit divers' email addresses
        uploaded to the database.<br/>
        In case you need to change the email address you can either:
    </p>
    <ol>
        <li>
            delete the diver from the database through the admin portal and add their data again with the new email
            address
        </li>
        <li>
            write an email to CMAS AquaLink support (email is given below) with a request, and we will make the
            corrections
        </li>
    </ol>
    <p>
        <b>How could a diver from your Federation register at CMAS AquaLink</b><br/>
        <b>Case 1:</b> Diver's data is uploaded in advance<br/>
        Diver's actions:
    </p>
    <ol>
        <li>
            open the registration page:<br/>
            <@mailer.href url="${context_path}/diver-registration.html"/>
        </li>
        <li>
            enter their CMAS card number
        </li>
        <li>
            tick the box "By ticking this box I hereby acknowledge and confirm my consent of AquaLink Privacy Policy"
            and presses the "Next" button
        </li>
        <li>
            The CMAS AquaLink System finds the diver using the entered CMAS card number, and provides them with further
            instructions
        </li>
    </ol>
    <p>
        Please kindly note that it is the Federation's responsibility to enter the diver's CMAS card number
        correctly.<br/>
        If the System fails to find the diver using their CMAS card number, the diver can try to register using their
        first name, last name and birthday by ticking the "I forgot or don't have CMAS certificate" tick box. In this
        case please kindly note that it is the Federation's responsibility to enter the diver's data correctly.
    </p>
    <p>
        <b>Case 2:</b> Diver's data is not uploaded by the National Federation<br/>
        If the System fails to find the diver using their first name, last name and birthday, the diver will be given
        DEMO access to the CMAS AquaLink. With DEMO access diver can submit photos of their certificates for
        Federation's approval. Once the Federation approves the certificate approval requests, the diver is granted CMAS
        Basic access to CMAS AquaLink. CMAS Basic access allows diver to be verified online using the Diver Verification
        Service:<br/>
        <@mailer.href url="${context_path}/diver-verification.html"/><br/>
    </p>
    <p>
        Please kindly note that it is the Federation's responsibility to review and approve or decline certificate
        approval requests from the Federation's diver.
    </p>
    <p>
        If you have any questions regarding this process please do not hesitate to contact us any time.
    </p>
    <p>
        Sincerely yours, <br/>
        Alexander Petukhov<br/>
        CMAS AquaLink support team<br/>
        Email: ${supportEmail}<br/>
        Mob UK: +44 7570 819047<br/>
        Mob RUS, WhatsApp, Telegram: +7 926 6656463<br/>
        Skype: sonsunich<br/>
    </p>
</@mailer.mail>

