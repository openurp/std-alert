[#ftl]
[@b.head/]

[@b.form name="alertListForm" method="post" action=""]
    [@b.grid items=alerts var="alert"]
        [@b.gridbar]
            bar.addItem("自动统计", "autoStat()");
            bar.addItem("查看不及格课程信息", "grades()");
            bar.addItem("查看帮扶记录", "record()");
            bar.addItem("${b.text("action.export")}",action.exportData("semester.code:学年学期,std.code:学号,std.name:姓名,std.state.squad.name:班级,std.state.grade:年级,std.state.department.name:院系,std.state.major.name:专业,std.state.direction.name:方向,alertType.name:预警类型,detail:情况说明",null,'fileName=学业预警详细名单'));
            bar.addItem("删除",action.remove("确认删除?"));
        [/@]
        [@b.row]
            [@b.boxcol /]
            [@b.col width="10%" property="std.code" title="学号"/]
            [@b.col width="8%" property="std.name" title="姓名"/]
            [@b.col property="std.state.squad.name" title="班级"/]
            [@b.col width="7%" property="std.state.grade" title="年级"/]
            [@b.col width="15%" property="std.state.department.name" title="院系"/]
            [@b.col width="10%" property="alertType.name" title="预警类别"/]
            [@b.col width="30%" property="detail" title="情况说明" style="white-space:pre"]
                [@b.a href="!grades?alertIds="+alert.id]${alert.detail!}[/@]
            [/@]
        [/@]
    [/@]
[/@]
<script>
  function autoStat() {
    var ids = bg.input.getCheckBoxValues("alert.id");
    if (ids == null || ids == "") {
      alert("请选择记录进行操作!");
      return;
    }
    bg.form.addInput(document.alertListForm, "alertIds", ids);
    bg.form.submit(document.alertListForm, "${b.url('!autoStat')}");
  }

  function grades() {
    var ids = bg.input.getCheckBoxValues("alert.id");
    if (ids == null || ids == "") {
      alert("请选择记录进行操作!");
      return;
    }
    if (ids.indexOf(",") != -1) {
      alert("请选择一条记录进行操作");
      return;
    }
    bg.form.addInput(document.alertListForm, "alertIds", ids);
    bg.form.submit(document.alertListForm, "${b.url('!grades')}");
  }

  function record() {
    var ids = bg.input.getCheckBoxValues("alert.id");
    if (ids == null || ids == "") {
      alert("请选择记录进行操作!");
      return;
    }
    if (ids.indexOf(",") != -1) {
      alert("请选择一条记录进行操作");
      return;
    }
    bg.form.addInput(document.alertListForm, "alertIds", ids);
    bg.form.submit(document.alertListForm, "${b.url('record!search')}", "_blank");
  }
</script>
[@b.foot/]
