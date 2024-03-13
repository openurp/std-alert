[#ftl]
[@b.head/]
[@b.toolbar title="帮扶记录"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="recordSearchForm" action="!search" target="recordlist" title="ui.searchForm" theme="search"]
      [@base.semester name="record.semester.id" label="学年学期" value=semester /]
      [@b.textfield label="姓名" name="record.file.std.name" value="" /]
      [@b.textfield label="学号" name="record.file.std.code" value="" /]
      [@b.select name="record.file.std.state.department.id" label="院系" items=departments empty="..."/]
      [@b.textfield label="班级" name="record.file.std.state.squad.name" value="" /]
      <input type="hidden" name="orderBy" value="file.std.code"/>
    [/@]
      </div>
      <div class="search-list">
    [@b.div id="recordlist" href="!search?orderBy=file.std.code & record.semester.id="+semester.id/]
      </div>
</div>
[@b.foot/]
