package org.cmas.presentation.entities.user;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.Role;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Entity
@Table(name = "registration")
public class Registration implements InternetAddressOwner, Validatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date dateReg;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    @Column(nullable = false)
    private String email;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    @Column(nullable = false)
    private String country;

    @Column
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String firstName;

    @Column(nullable = false)
    private Date dob;

    @Column
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String lastName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    @Column(nullable = false)
    private String role;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String password;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String md5;

    @Column(nullable = false)
    private Locale locale;

    @Expose
    @Column
    private String areaOfInterest;

    @OneToMany(mappedBy = "registration")
    private List<RegFile> images;

    @ManyToOne
    private NationalFederation federation;

    public Registration() {
    }

    public Registration(Date dateReg) {
        this.dateReg = dateReg;
    }

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors, role, Role.class, "role", "validation.incorrectField");
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    @Override
    public Long getNullableId() {
        return id;
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        String personal = email;
        return new InternetAddress(email, personal, "UTF-8");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<RegFile> getImages() {
        return images;
    }

    public void setImages(List<RegFile> images) {
        this.images = images;
    }

    public NationalFederation getFederation() {
        return federation;
    }

    public void setFederation(NationalFederation federation) {
        this.federation = federation;
    }
}
