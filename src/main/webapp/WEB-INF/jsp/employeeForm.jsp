<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>employeeForm</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script type="text/javascript">
$(function() {
   /*  Submit form using Ajax */
   $('button[type=submit]').click(function(e) {
   
       //stop submit the form, we will post it manually.
	   e.preventDefault();
      
      $('input').next().remove();
      
      $.post({
         url : 'saveEmployee',
         data : $('form[name=employeeForm]').serialize(),
         success : function(res) {
               
                var json = "<h4>Ajax Response</h4><pre>"+ JSON.stringify(res.employee) + "</pre>";
               $('#feedback').html(json);
               
               console.log("SUCCESS : ", res);
               $("#feedback").prop("disabled", false);
            
         }
      })
   });
});
</script>
</head>
<body>
  <h1>Employee Input Form</h1>
  <hr />

  <!-- <form action="saveEmployee" method="post" name="employeeForm"> -->
  <form name="employeeForm">
    <table>
      <tr>
        <td>First Name</td>
        <td><input name="firstName" type="text" /></td>
      </tr>
      <tr>
        <td>Last Name</td>
        <td><input name="lastName" type="text" /></td>
      </tr>
      <tr>
        <td>Email</td>
        <td><input name="email" type="text" /></td>
      </tr>
      <tr>
        <td>Designation</td>
        <td><input name="designation" type="text" /></td>
      </tr>
      <tr>
        <td></td>
        <td><button type="submit">Submit</button></td>
      </tr>
    </table>
  </form>

	<!-- Result Container  -->
	<div id="feedback"></div>
	
</body>
</html>