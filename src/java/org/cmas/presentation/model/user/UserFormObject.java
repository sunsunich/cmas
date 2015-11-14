package org.cmas.presentation.model.user;

import org.cmas.Globals;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.Transferable;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Pattern;

public class UserFormObject implements Transferable<UserClient> {


	private String region;

    @NotEmpty(message = "validation.emptyField")
	private String city;

    @NotEmpty(message = "validation.emptyField")
    @Pattern( regex = Globals.PHONE_REGEXP
            , message = "validation.phoneValid"
    )
	private String phone;

    private String address;

	private String postCode;


	@Override
    public void transferToEntity(UserClient entity) {

    }

    @Override
    public void transferFromEntity(UserClient entity) {



    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
