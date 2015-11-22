(function($) {
	function displayImgLoader(flag) {
        if(flag) {
            $('#content-img-load .img-load').html("<img src='assets/img/img-load.gif' />");
        } else {
            $('#content-img-load .img-load').html("");
        }
    }

	$(".tt input").blur(function() {
		var tds = $(this).closest("tr").find("td"),
        contactid = tds.eq(0).attr("contactid"),
        name = tds.eq(1).find("input").val(),
        phone = tds.eq(2).find("input").val();

	    datas = []; cols = {};
		cols['contactid'] = contactid;
		cols['name'] = name;
		cols['phone'] = phone;
		datas = cols;
	
	    $.ajax({
	        type: 'POST',
	        url: "interfaceAnnuaire",
	        data: "updatecontact=0&datas="+JSON.stringify(datas),
	        dataType: 'json',
	        cache: false,
	        beforeSend: function() {
	            displayImgLoader(true);
	        },
	        success: function (datas) {
	            displayImgLoader(false);
	            if(datas.type === -1){
	            	$('#alerte').removeClass("alert-success");
	            	$('#alerte').addClass("alert-danger").text(datas.message);
	            }else{
	            	$('#alerte').removeClass("alert-danger");
	                $('#alerte').addClass("alert-success").text(datas.message);
	            }
	        },
	        error:  function (jqXHR, textStatus, errorThrown) {
	            displayImgLoader(false);
	            alert("Error : " + jqXHR.status + " - " + errorThrown + " - " + textStatus);
	        }
	    });
	});

	$('#author').click(function() {
		swal("Phoenix Group 2015", "TABUE ROMEO\nTUENO FOTSO STEVE\nMOUAFFO KENFACK REINE\nTEGANTCHOUANG TEUKAP BORIS", "info");
	});

    $('.bt-save').click(function() {
        /*var tds = $(this).closest("tr").find("td"),
            contactid = tds.eq(0).attr("contactid"),
            name = tds.eq(1).find("input").val(),
            phone = tds.eq(2).find("input").val();

        datas = []; cols = {};
    	cols['contactid'] = contactid;
    	cols['name'] = name;
    	cols['phone'] = phone;
    	datas = cols;

        $.ajax({
            type: 'POST',
            url: "interfaceAnnuaire",
            data: "updatecontact=0&datas="+JSON.stringify(datas),
            dataType: 'json',
            cache: false,
            beforeSend: function() {
                displayImgLoader(true);
            },
            success: function (datas) {
                displayImgLoader(false);
                if(datas.type === -1){
                	$('#alerte').removeClass("alert-success");
                	$('#alerte').addClass("alert-danger").text(datas.message);
                }else{
                	$('#alerte').removeClass("alert-danger");
                    $('#alerte').addClass("alert-success").text(datas.message);
                }
            },
            error:  function (jqXHR, textStatus, errorThrown) {
                displayImgLoader(false);
                alert("Error : " + jqXHR.status + " - " + errorThrown + " - " + textStatus);
            }
        });*/
    });

    $('.bt-delete').click(function() {
    	var tr =  $(this).closest("tr"),
    	tds = tr.find("td"),
        contactid = tds.eq(0).attr("contactid");

    	datas = []; cols = {};
    	cols['contactid'] = contactid;
    	datas = cols;

    	swal({   
            title: "Request confirmation",
            text: "Do you want really delete this contact ?",   
            type: "warning",   
            showCancelButton: true,   
            confirmButtonColor: "#DD6B55",   
            cancelButtonText: "Annuler",
            confirmButtonText: "Supprimer",
            closeOnConfirm: true,   
            closeOnCancel: false }, 
            function(isConfirm){
                if (isConfirm) {
                	$.ajax({
            	        type: 'POST',
            	        url: "interfaceAnnuaire",
            	        data: "deletecontact=0&datas="+JSON.stringify(datas),
            	        dataType: 'json',
            	        cache: false,
            	        beforeSend: function() {
            	            displayImgLoader(true);
            	        },
            	        success: function (datas) {
            	            displayImgLoader(false);
            	            if(datas.type === -1){
            	            	$('#alerte').removeClass("alert-success");
            	            	$('#alerte').addClass("alert-danger").text(datas.message);
            	            }else{
            	            	$('#alerte').removeClass("alert-danger");
            	                $('#alerte').addClass("alert-success").text(datas.message);

            	                tr.remove();
            	            }
            	        },
            	        error:  function (jqXHR, textStatus, errorThrown) {
            	            displayImgLoader(false);
            	            alert("Error : " + jqXHR.status + " - " + errorThrown + " - " + textStatus);
            	        }
            	    });
                } else {
                    swal("Information", "Operation cancelled !", "info");
                } 
            });
    });
}(jQuery));