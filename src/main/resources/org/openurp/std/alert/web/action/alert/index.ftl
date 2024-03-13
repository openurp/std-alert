[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='学生预警情况']
  bar.addItem("汇总", "summary()");

  function summary() {
  bg.form.submit(document.indexForm,  "${b.url("depart-summary")}", "_blank");
  }
[/@]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="alertSearchForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@base.semester name="alert.semester.id" label="学年学期" value=semester /]
            [@b.textfield label="学号" name="alert.std.code" value="" /]
            [@b.textfield label="姓名" name="alert.std.name" value="" /]
            [@b.textfield label="年级" name="alert.std.state.grade" value="" /]
            [@b.select name="alert.std.state.department.id" label="院系" items=departments empty="..."/]
            [@b.textfield label="班级" name="alert.std.state.squad.name" value="" /]
            [@b.select name="alert.alertType.id" label="预警类型" items=alertTypes empty="..."/]
            [@b.select name="isGreen" items={} label="是否预警"]
              <option value="">...</option>
              <option value="0" selected="selected">有预警</option>
              <option value="1">未预警</option>
            [/@]
          <input type="hidden" name="orderBy" value="alert.std.code"/>
        [/@]
  </div>
  <div class="search-list">
        [@b.div id="contentDiv" href="!search?orderBy=alert.std.code&alert.semester.id="+semester.id + "&isGreen=0" /]
  </div>
</div>
[@b.foot/]
