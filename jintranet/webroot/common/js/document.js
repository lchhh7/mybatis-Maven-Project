var index = {
	init: function init() {
		var _this = this;

		$("#btn-create").on("click", function () {
			_this.create();
		});
		
		$("[id^=btn-deleteDocument]").on("click", function (e) {
			var id = e.target.value;
			_this.deleteDocument(id);
		});
	},

	create: function create() {
		var data = {
			title: $("#title").val()
		};

		$.ajax({
			type: "post",
			url: "/jintranet/document/saveProc.do",
			data: JSON.stringify(data),
			contentType: "application/json; charset=UTF-8",
			dataType: "json"
		}).done(function () {
				alert("등록에 성공하였습니다");
				window.location.href = window.location.href;
		}).fail(function (error) {
			alert(JSON.stringify(error));
		});
	},

	deleteDocument: function deleteDocument(id) {
		var data ={
			id : id
		};
		
		$.ajax({
			type: "put",
			url: "/jintranet/document/deleteProc.do",
			data : JSON.stringify(data),
			contentType : "application/json; charset=UTF-8",
			dataType: "json"
		}).done(function (resp) {
			if (resp.status !== 200) {
				alert("삭제에 실패하였습니다");
			} else {
				alert("삭제 성공");
				window.location.href = window.location.href;
			}
		}).fail(function (error) {
			alert(JSON.stringify(error));
		});
	}
};

index.init();