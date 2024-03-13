[#ftl]
[@b.head/]
<style>
  form.listform label.title {
    width: 120px
  }
</style>
[@b.toolbar title="编辑帮扶记录"]bar.addBack();[/@]
[@b.tabs]
    [#assign sa][#if record.persisted]!update?id=${record.id!}[#else]!save?alert.id=${alert.id}[/#if][/#assign]
    [@b.form action=sa theme="list" enctype="multipart/form-data"]
        [@base.semester name="record.semester.id" label="学年学期" value=semester /]
        [@b.textfield name="record.name" label="记录名称" value="${record.name!}" required="true"/]
        [@b.textfield name="record.std.code" label="学号" value="${(alert.std.code)!}" disabled = "disabled"/]
        [@b.textfield name="record.std.name" label="姓名" value="${(alert.std.name)!}" disabled = "disabled"/]
        [@b.textfield name="record.alertType.name" label="预警类别" value="${(alert.alertType.name)!}" disabled = "disabled"/]
        [@b.textfield name="record.detail" label="预警情况说明" value="${(alert.detail)!}" disabled = "disabled"/]
        [@b.textarea name="record.description" label="记录" value="${record.description!}" required="true" rows="20" cols="100"/]
        [@b.field label="附件" ]
          <td>
            <input name="attachment" type="file"/>

              [#if record.attachments??]
                  [#list record.attachments as attachment]
                    <a
                    href="${b.url("!attachment?attachmentId="+attachment.id)}">${(attachment.name)!}</a>[#if attachment_has_next]
                    ；[/#if]
                  [/#list]
              [/#if]
          </td>

        [/@]
        [@b.formfoot]
            [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
        [/@]
    [/@]
[/@]
[@b.foot/]
