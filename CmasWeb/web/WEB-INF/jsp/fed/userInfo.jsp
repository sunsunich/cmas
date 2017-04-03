
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="user" scope="request" type="org.cmas.entities.User"/>

<my:fed_adminpage title="Diver Info">
    <h2>Diver Info</h2>
    <form style="line-height: 200%">
        <div class="width70">
        <label class="inpadm">E-mail</label>
        <input type="email" name="email">
        </div>
        <div class="width70">
        <label class="inpadm">First Name</label>
        <input type="text" name="First Name">
        </div>
        <div class="width70">
        <label class="inpadm">Last Name</label>
        <input type="text" name="Last Name">
            </div>
        <div class="width70">
        <label class="inpadm">Date of birth</label>
        <input type="date" name="Date of birth">
            </div>
        <div class="width70">
        <label class="inpadm">Instructor or Diver</label>
        <select>
            <option value="Instructor">Instructor</option>
            <option value="Diver">Diver</option>
        </select>
            </div>
        <div class="width70">
        <label class="inpadm">Diver Level</label>
        <select>
            <option value="One Star">ONE_STAR</option>
            <option value="Two Star">TWO_STAR</option>
            <option value="Three Star">THREE_STAR</option>
            <option value="Four Star">FOUR_STAR</option>
        </select></div>
        <div class="width70">
        <label class="inpadm">CMAS card number</label></div>

        <div class="width70">
        <label class="inpadm">Number card National Federation</label>
        <input type="number" name="Number card National Federation"></div>
        <div class="width70">
            <label class="inpadm">Number card National Federation Instructor</label>
            <input type="number" name="Number card National Federation Instructor"></div>
        <div class="width70">
            <label class="inpadm"><b>Certification</b></label></div>
        <div class="width70">
        <table style="margin-left: 30px">
            <tr>
                <td><label style="margin:0 10px 0 20px">Type of certificate</label></td>
                    <td><select>
                        <optgroup label="Scientific">
                            <option value="First type">First type</option>
                            <option value="Second tupe">Second type</option>
                        </optgroup>
                        <optgroup label="Technical">
                            <option value="First type">First type</option>
                            <option value="Second type">Second type</option>
                        </optgroup>
                        <optgroup label="Ordinary">
                            <option value="First type">First type</option>
                            <option value="Second type">Second type</option>
                        </optgroup>
                    </select></td>
                <td><label style="margin:0 10px 0 20px"><b>Added Certifications</b></label></td>
                <td></td>
            </tr>
            <tr>
                <td><label style="margin:0 10px 0 20px">Instructor or Diver</label></td>
                    <td><select>
                        <option value="Instructor">Instructor</option>
                        <option value="Diver">Diver</option>
                    </select></td>
                <td><label style="margin:0 10px 0 20px">Biological one star scientist certificate two star </label></td>
                    <td><button type="button">Delete certificate</button></td>
            </tr>
            <tr>
                <td><label style="margin:0 10px 0 20px">Level</label></td>
                    <td><select>
                        <option value="One Star">ONE_STAR</option>
                        <option value="Two Star">TWO_STAR</option>
                        <option value="Three Star">THREE_STAR</option>
                        <option value="Four Star">FOUR_STAR</option>
                    </select></td>
                <td><label style="margin:0 10px 0 20px">Second certificate</label></td>
                   <td><button type="button">Delete certificate</button></td>
            </tr>
            <tr>
                <td><label style="margin:0 10px 0 20px">Number of certificate</label></td>
                    <td><input type="number" name="Number of certificate"></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td><button type="button" name="Add Certificate">Add Certificate</button></td>
                <td></td>
                <td></td>
            </tr>

        </table></div>
        <div class="width70">
        <button type="submit" class="buttonadm">Add Driver</button>
            </div>
    </form>

</my:fed_adminpage>