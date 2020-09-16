# next version

* 通过weChat Library bug测试得出core需要添加部分开放api

# core

## 0.1.5

* 增加LayoutManager扩展
* 提高scan版本至0.1.5
* 去除部分无用api
* 提高targetSdk到30

## 0.1.4

* 修复 ASC 排序finder数据显示问题
* 剥离Fragment
* 支持屏蔽内置toast

## 0.1.3

* 预览页提供刷新数据api
* 预览页提供单独扫描api
* 修复预览页自定义排序无效
* 支持对Fragment的继承

## 0.1.2

* 新增对排序条件的支持
* 去除kotlinx依赖
* 提高viewholder为0.0.6，改进了tag
* 修复获取目录时数据错乱
* 剥离裁剪功能，仅提供接口，由 ui Library提供实现
* 修复点击预览页扫描问题
* 预览页修改为ViewModel获取数据
* 部分回调去除context空检查

# ui

## 0.1.0

* 支持 core 0.1.5版本

## 0.0.9

* 修复 ASC 排序finder数据显示问题

## 0.0.8

* 提供对Fragment替换api
* 提高core版本号，提供对PrevFragment的扩展

## 0.0.7

* 提高core版本
* 优化裁剪逻辑

# scan 

## 0.1.5

* 去掉内置ScanType，改为 MediaStore.Files.FileColumns 获取 IntArray
* 去掉内置Columns，改为自定义Columns
* 支持自定义实体类，内置文件扫描，图片扫描，音频扫描
* 支持多种扫描格式
* 去除FragmentActivity限制，由owner获取LoaderManager
* 提高targetSdk到30

## 0.1.4

* 新增 date_added 字段
* 将 dataModified 修改为 dateModified
* 新增扫描异常回调

## 0.1.3

* 更新viewModel参数

## 0.1.2

* 增加ViewModel实现类

## 0.1.1

* kotlinx版本提升至0.0.6
* kotlinx依赖改为api
* 新增对排序条件的支持