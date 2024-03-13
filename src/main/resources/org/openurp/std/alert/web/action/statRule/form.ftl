[#ftl]
[@b.head/]
[@b.toolbar title="新建/修改统计规则"]bar.addBack();[/@]
[@b.tabs]
  [#assign sa][#if statRule.persisted]!update?id=${statRule.id!}[#else]!save[/#if][/#assign]
  [@b.form action=sa theme="list"]
    [@b.textfield name="statRule.name" label="名称" value="${statRule.name!}" required="true" /]
    [@b.select name="statRule.method.id" label="统计方法" value="${(statRule.method.id)!}"
               style="width:200px;" items=methods option="id,name" empty="..." required="true"/]
    [@b.textfield name="statRule.maxValue" label="不及格学分上限" value="${statRule.maxValue!}" required="true" /]
    [@b.textfield name="statRule.minValue" label="不及格学分下限" value="${statRule.minValue!}" required="true" /]
    [@b.select name="statRule.alertType.id" label="预警类型" value="${(statRule.alertType.id)!}"
               style="width:200px;" items=alertTypes option="id,name" empty="..."/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]