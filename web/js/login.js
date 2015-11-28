fieldsAreDisabled = false;
/**
 Bekapcsolja a jelszóemlékeztető panelt
 */
function ShowForgetPassword()
{
    if (fieldsAreDisabled==true) return false;

    var objFPP = document.getElementById('forgetPasswordPanel');
    var objLP = document.getElementById('loginPanel');
    var login =	document.getElementById('txbLogin').value;
    if (objFPP!==null) objFPP.style.display = 'block';
    if (objLP!==null) objLP.style.display = 'none';
    document.getElementById('txbEmail2').value = login;
    document.getElementById('txbEmail2').focus();
}
/**
 Bekapcsolja a belépés panelt
 */
function ShowSignIn()
{
    if (fieldsAreDisabled==true) return false;

    var objFPP = document.getElementById('forgetPasswordPanel');
    var objLP = document.getElementById('loginPanel');
    //var email =	document.getElementById('txbEmail2').value;
    if (objFPP!==null) objFPP.style.display = 'none';
    if (objLP!==null) objLP.style.display = 'block';
    //document.getElementById('txbLogin').value = email;
    document.getElementById('txbLogin').focus();
}
/**
 Bekapcsolja a regisztráció panelt
 */
function ShowRegistration()
{
    if (fieldsAreDisabled==true) return false;

    var objRP = document.getElementById('registrationPanel');
    var objRIP = document.getElementById('regInfoPanel');

    if (objRP!==null) objRP.style.display = 'block';
    if (objRIP!==null) objRIP.style.display = 'none';
    document.getElementById('txbEmail').focus();
}
/**
 Bekapcsolja a regisztráció visszaigazolásának paneljét
 @argEmail	string		A regisztrációban megadott e-mail
 @argMessage	string		Üzenet
 */
function ShowRegistrationOk(argEmail,argMessage)
{
    var objRP = document.getElementById('registrationPanel');
    var objROP = document.getElementById('registrationOkPanel');
    var objFPP = document.getElementById('forgetPasswordPanel');
    var objLP = document.getElementById('loginPanel');
    var objErr1 = document.getElementById('errorMessage1');
    var objErr2 = document.getElementById('errorMessage2');

    SetFieldsToDisabled(false);

    if (objRP!==null) objRP.style.display = 'none';
    if (objROP!==null) objROP.style.display = 'block';
    if (objFPP!==null) objFPP.style.display = 'none';
    if (objLP!==null) objLP.style.display = 'block';
    if (objErr1!=null){
        if (argMessage!='') objErr1.innerHTML = argMessage;
        else objErr1.innerHTML = '&nbsp;';
    }
    if (objErr2!=null) objErr2.innerHTML = '&nbsp;';
    document.f.txbLogin.value = argEmail;
    document.f.txbPassword.focus();
}
/**
 Elküldi a regisztrációs adatokat
 */
function SignIn()
{
    SetFieldsToDisabled(true);
    document.getElementById('login-anim').style.display = 'block';

    var parameters = 'btnLogin=1';
    var objMessage = document.getElementById('errorMessage1');
    parameters+= '&txbLogin='+escape(document.getElementById('txbLogin').value);
    parameters+= '&txbPassword='+escape(document.getElementById('txbPassword').value);

    objMessage.innerHTML = '';

    client = new HttpClient();
    client.isAsync = true;
    client.requestType = 'POST';
    client.callback = function(argResult){
        eval ('result = '+argResult);
        if (result){
            if (result.redirect) window.location.href = result.redirect;
            else{
                var objMessage = document.getElementById('errorMessage1');
                objMessage.innerHTML = result.message;
                SetFieldsToDisabled(false);
                document.getElementById('login-anim').style.display = 'none';
            }
        }
    };
    client.makeRequest('sign-in', parameters, null);
}
/**
 Elküldi a jelszóemlékeztetőt
 */
function SendEmail()
{
    SetFieldsToDisabled(true);
    document.getElementById('send-anim').style.display = 'block';

    var parameters = 'btnSend=1';
    var objMessage = document.getElementById('errorMessage1');
    parameters+= '&txbEmail2='+escape(document.getElementById('txbEmail2').value);

    objMessage.innerHTML = '';

    client = new HttpClient();
    client.isAsync = true;
    client.requestType = 'POST';
    client.callback = function(argResult){
        eval ('result = '+argResult);
        if (result.success){
            var objPanel = document.getElementById('forgetPasswordPanel');
            objPanel.innerHTML = result.message;
        }else{
            var objMessage = document.getElementById('errorMessage1');
            objMessage.innerHTML = result.message;
        }
        SetFieldsToDisabled(false);
        document.getElementById('send-anim').style.display = 'none';
    };
    client.makeRequest('sign-in', parameters, null);
}
/**
 Elküldi a regisztrációt
 */
function CreateAccount()
{

    SetFieldsToDisabled(true);
    document.getElementById('registration-anim').style.display = 'block';

    var parameters = 'btnReg=1';
    var objMessage = document.getElementById('errorMessage2');
    var objSelect = document.getElementById('oplCountries');
    parameters+= '&txbEmail='+escape(document.getElementById('txbEmail').value);
    parameters+= '&oplCountries='+objSelect.options[objSelect.selectedIndex].value;

    if (!CheckEmail(document.getElementById('txbEmail').value)){
        objMessage.innerHTML = invalidEmailMessage;
        document.getElementById('registration-anim').style.display = 'none';
        SetFieldsToDisabled(false);
        return false;
    }

    objMessage.innerHTML = '';

    client = new HttpClient();
    client.isAsync = true;
    client.requestType = 'POST';
    client.callback = function(argResult){
        eval ('result = '+argResult);
        if (result.success){
            var objPanel = document.getElementById('registrationPanel');
            objPanel.innerHTML = result.message;
        }else{
            var objMessage = document.getElementById('errorMessage2');
            objMessage.innerHTML = result.message;
        }
        SetFieldsToDisabled(false);
        document.getElementById('registration-anim').style.display = 'none';
    };
    client.makeRequest('sign-in', parameters, null);
}
/**
 Ellenőrzi az e-mail címet, hogy meg van-e adva és érvényes formátumú-e
 @argEmail	string		E-mail
 @return		bool		True/false
 */
function CheckEmail(argEmail)
{
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    return reg.test(argEmail);
}
/**
 Beállítja a beviteli mezők állapotát, hogy legyenek letiltva
 @argState	bool	A letiltás (true, false)
 */
function SetFieldsToDisabled(argState)
{
    var arrFields = new Array('txbEmail','txbEmail2','oplCountries','btnLogin','btnSend','btnReg','txbPassword');
    var objFie;
    var objRP = document.getElementById('registrationPanel');
    var objLP = document.getElementById('loginPanel');
    var objFPP = document.getElementById('forgetPasswordPanel');

    fieldsAreDisabled = argState;

    for (i=0;i<arrFields.length;i++){
        objFie = document.getElementById(arrFields[i]);
        if (objFie!==null) objFie.disabled = argState;
    }

    if (objRP!=null){
        if (argState == true) objRP.className = 'fadeit';
        else objRP.className = '';
    }
    if (objFPP!=null){
        if (argState == true) objFPP.className = 'fadeit';
        else objFPP.className = '';
    }
    if (objLP!=null){
        if (argState == true) objLP.className = 'fadeit';
        else objLP.className = '';
    }
    if (argState==true){
        objFie = document.getElementById('btnLogin_clicked');
        if (objFie!== null) objFie.value = '';
        objFie = document.getElementById('btnReg_clicked');
        if (objFie!== null) objFie.value = '';
        objFie = document.getElementById('btnSend_clicked');
        if (objFie!== null) objFie.value = '';
        objFie = document.getElementById('btnSend_clicked');
        if (objFie!== null) objFie.value = '';
    }
}
/**
 Ellenőrzi az ENTER ütést
 */
function CheckEnter(e){
    var characterCode;
    if(e && e.which){
        e = e;
        characterCode = e.which;
    }else{
        e = event;
        characterCode = e.keyCode;
    }
    if(characterCode == 13) return true;
    return false;
}
/**
 * Ellenőrzi a profil oldalon a jelszavak egyezőségét
 */
function CheckPasswords()
{
    var objP1 = document.getElementById('newpassword1');
    var objP2 = document.getElementById('newpassword2');
    var objPwd1 = document.getElementById('txbNewPassword1');
    var objPwd2 = document.getElementById('txbNewPassword2');
    var objB = document.getElementById('btnChangePassword');
    objP1.className = 'pipe_invalid';
    objP2.className = 'pipe_invalid';
    objB.disabled = true;
    if (objPwd1.value.length>5) objP1.className = 'pipe_valid';
    if ((objPwd2.value.length>5) && (objPwd1.value==objPwd2.value)){
        objP2.className = 'pipe_valid';
        objB.disabled = false;
    }
}
/**
 * Elküldi a jelszócserét
 */
function ChangePassword()
{
    SetFieldsToDisabled(true);
    document.getElementById('send-anim').style.display = 'block';

    var parameters = 'btnChangePassword=1';
    var objMessage = document.getElementById('errorMessage');
    parameters+= '&txbOldPassword='+escape(document.getElementById('txbOldPassword').value);
    parameters+= '&txbNewPassword1='+escape(document.getElementById('txbNewPassword1').value);
    parameters+= '&txbNewPassword2='+escape(document.getElementById('txbNewPassword2').value);

    objMessage.innerHTML = '';

    client = new HttpClient();
    client.isAsync = true;
    client.requestType = 'POST';
    client.callback = function(argResult){
        eval ('result = '+argResult);
        if (result.success){
            document.getElementById('txbOldPassword').value = '';
            document.getElementById('txbNewPassword1').value = '';
            document.getElementById('txbNewPassword2').value = '';
            alert(result.message);
        }else{
            var objMessage = document.getElementById('errorMessage');
            objMessage.innerHTML = result.message;
        }
        document.getElementById('send-anim').style.display = 'none';
    };
    client.makeRequest('profil-edit', parameters, null);
}


