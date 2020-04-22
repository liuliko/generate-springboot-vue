<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>${subpackage}</span>
        <el-button type="primary" icon="el-icon-download" style="float: right;margin-right: 35px" size="small"
                   @click="excel">excel
        </el-button>
        <el-button type="danger" icon="el-icon-delete" style="float: right;margin-right: 10px" size="small"
                   @click="deleteApiBatch">删除
        </el-button>
        <el-button type="success" icon="el-icon-plus" style="float: right;margin-right: 10px" size="small"
                   @click="addApi">新增
        </el-button>
      </div>
      <el-form :inline="true" :model="params" size="mini">
        <#list table.columns as column>
        <#if column.columnNameLower != "id">
        <#if (column.remarks)??>
        <el-form-item label=" ${(column.remarks)!""}" label-width="20">
          <el-input v-model="params.${column.columnNameLower}" autocomplete="off"></el-input>
        </el-form-item>
        <#else>
        <el-form-item label="> ${column.columnNameLower}" label-width="20">
          <el-input v-model="params.${column.columnNameLower}" autocomplete="off"></el-input>
        </el-form-item>
          </#if>
        </#if>
         </#list>
        <el-form-item>
          <el-button type="primary" @click="$refs.listTable.fetchData()" size="small">查询</el-button>
        </el-form-item>
      </el-form>
      <vTable ref="listTable" fetchUrl="/${subpackage}/${classNameLower!""}/list.html" :colums="colums" :params="params"
              :buttons="buttons"></vTable>
    </el-card>
    <el-dialog width="30%" title="用户信息" :visible.sync="dialogFormVisible" @close="resetFormField">
      <el-form :model="apifrom" size="mini" :rules="rules" ref="apiform">
        <#list table.columns as column>
        <#if column.columnNameLower != "id">
        <#if (column.remarks)??>
        <el-form-item label=" ${(column.remarks)!""}"  :label-width="formLabelWidth" prop="${column.columnNameLower}">
        <el-input v-model="apifrom.${column.columnNameLower}" autocomplete="off"></el-input>
        </el-form-item>
        <#else>
        <el-form-item label="> ${column.columnNameLower}"  :label-width="formLabelWidth" prop="${column.columnNameLower}">
          <el-input v-model="apifrom.${column.columnNameLower}" autocomplete="off"></el-input>
        </el-form-item>
            </#if>
          </#if>
      </#list>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="formcommit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import vTable from '@/components/table/table'
  import request from '@/utils/request'
  import { createLoading } from '@/api/loading'
  import { createNotification } from '@/api/notification'

  export default {
    components: { vTable },
    data() {
      return {
        colums: [
                <#list table.columns as column>
                <#if column.columnNameLower != "id">
                <#if (column.remarks)??>
                {label: '${column.remarks}', align: 'left', formatter: function(row, col, cellvalue, index) {return row.${column.columnNameLower}}},
              <#else>
                  {label: '${column.columnNameLower}', align: 'left', formatter: function(row, col, cellvalue, index) {return row.${column.columnNameLower}}},
                </#if>
              </#if>
              </#list>
        ],
        buttons: [
          {
            label: '修改',
            type: 'primary',
            click: this.editApi
          },
          {
            label: '删除',
            type: 'danger',
            click: this.deltetApi
          }
        ],
        params: {
      <#list table.columns as column>
        ${column.columnNameLower}: '',
      </#list>
        },
        apifrom: {
        <#list table.columns as column>
          ${column.columnNameLower}: '',
        </#list>
        },
        rules: {
          <#list table.columns as column>
          <#if column.columnNameLower != "id">
          <#if (column.remarks)??>
          ${column.columnNameLower}: [
          { required: true, message: '请输入${column.remarks}', trigger: 'blur' },
                  { max: 64, message: '请勿输入超过64个字符', trigger: 'blur' } ],
        <#else>
          ${column.columnNameLower}: [
            { required: true, message: '请输入${column.columnNameLower}', trigger: 'blur' },
            { max: 64, message: '请勿输入超过64个字符', trigger: 'blur' } ],
        </#if>
          </#if>
          </#list>
        },
        dialogFormVisible: false,
        formLabelWidth: '80px'
      }
    },
    methods: {
      editApi: function(row, index) {
        for (var key in this.apifrom) {
          console.log(key, row)
          this.apifrom[key] = row[key]
        }
        this.dialogFormVisible = true
      },
      deltetApi: function(row, index) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        })
          .then(_ => {
            let ids = []
            ids.push(row.id)
            let loading = createLoading()
            request({
              url: '/${subpackage}/${classNameLower!""}/deletes.html', method: 'POST', params: { ids: ids }
            }).then(data => {
              loading.close()
              if (data.data != -1) {
                this.$refs.listTable.fetchData()
                createNotification('删除成功','success')
                this.dialogFormVisible = false
              } else {
                console.log(data)
                this.$message({
                  message: '操作失败,请稍后再试',
                  type: 'error'
                })
              }
            }).catch(() => {
              loading.close()
            })

          })
          .catch(_ => {
          })
      },
      deleteApiBatch: function() {
        var rows = this.$refs.listTable.getSelectedData()

        if (!rows || rows.length == 0) {
          createNotification('请至少选择一项以进行删除','warning')
          return
        }
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        })
          .then(_ => {
            let ids = []
            for (let i = 0; i < rows.length; i++) {
              ids.push(rows[i].id)
            }
            let loading = createLoading()
            request({
              url: '/${subpackage}/${classNameLower!""}/deletes.html',
              method: 'post',
              params: { ids: ids }
            }).then(data => {
              loading.close()
              if (data.data != -1) {
                this.$refs.listTable.fetchData()
                createNotification('删除成功','success')
                this.dialogFormVisible = false
              } else {
                console.log(data)
                createNotification('操作失败,请稍后再试','error')
              }
            }).catch(() => {
              loading.close()
            })

          })
          .catch(_ => {
          })
      },
      addApi: function() {
        for (var key in this.apifrom) {
          this.apifrom[key] = ''
        }
        this.dialogFormVisible = true
      },
      formcommit: function() {
        let vApiform = this.$refs.apiform
        vApiform.validate(valid => {
          if (valid) {
            let url = this.apifrom.id ? '/${subpackage}/${classNameLower!""}/update.html' : '/${subpackage}/${classNameLower!""}/insert.html'
            let loading = createLoading()
            request({
              url: url,
              method: 'post',
              params: this.apifrom
            }).then(data => {
              loading.close()
              if (data.data != -1) {
                this.$refs.listTable.fetchData()
                createNotification('保存成功','success')
                this.dialogFormVisible = false
              } else {
                console.log(data)
                createNotification('操作失败,请稍后再试','error')
              }
            }).catch(() => {
              loading.close()
            })

          } else {
            return false
          }
        })
      },
      excel: function() {

        let params = {}
        for (var item in this.params) {
          if (this.params[item] !== '') {
            let value = this.params[item]
            if (typeof value == 'string') {
              value = value.trim()
            }
            params[item] = value
          }
        }
        let loading = createLoading()
        request({
          url: '/${subpackage}/${classNameLower!""}/export.html',
          method: 'POST',
          responseType: 'blob',
          onDownloadProgress: function(progressEvent) {

          },
          params: params
        }).then((blob) => {
          this.downFile(blob, 'sys.xls')
          loading.close()
        }).catch((e) => {
          loading.close()
          console.log('error', e)
        })
      },
      downFile: function(blob, fileName) {
        // 非IE下载
        if ('download' in document.createElement('a')) {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob) // 创建下载的链接
          link.download = fileName // 下载后文件名
          link.style.display = 'none'
          document.body.appendChild(link)
          link.click() // 点击下载
          window.URL.revokeObjectURL(link.href) // 释放掉blob对象
          document.body.removeChild(link) // 下载完成移除元素
        } else {
          // IE10+下载
          window.navigator.msSaveBlob(blob, fileName)
        }
      },
    resetFormField() {
      this.$refs.apiform.resetFields()
    }
    }, mounted() {
    }
  }
</script>
