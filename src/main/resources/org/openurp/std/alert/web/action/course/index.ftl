[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='课程预警情况'/]
<div class="search-container">
  <div class="search-panel">
      [@b.form action="!search" name="alertSearchForm" title="ui.searchForm" target="contentDiv" theme="search"]
          [@base.semester name="alert.semester.id" label="学年学期" value=semester  /]
          [@b.textfield name="alert.course.code" label="课程代码" maxlength="32"/]
          [@b.textfield name="alert.course.name" label="课程名称" maxlength="32"/]
          [@b.select name="alert.course.department.id" label="院系" items=departments empty="..."/]
        <input type="hidden" name="orderBy" value="alert.count desc"/>
        <input type="hidden" name="alert.semester.id" value="${semester.id}"/>
      [/@]
  </div>
  <div class="search-list">
      [@b.div id="contentDiv" href="!search?orderBy=alert.count desc & alert.semester.id="+semester.id/]
  </div>
</div>
[@b.foot/]
