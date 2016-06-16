package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.ui.XMLCommand;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2016/1/12.
 */
public class CtrlActionUtilities {
	private CtrlActionUtilities() {
		// 不提供构造函数
	}

	// #region Variable
	//
	// #endregion
	//
	// #region Property
	// private static Keys[] keys;
	// public static Keys[] AllKeys
	// {
	// get
	// {
	// if (keys == null)
	// {
	// List<Keys> keys = new List<Keys>();
	// foreach (int i in Enum.getValues(typeof(Keys)))
	// {
	// keys.Add((Keys)i);
	// }
	// keys = keys.ToArray();
	// }
	// return keys;
	// }
	// }

	/**
	 * 根据具体参数构造CtrlAction
	 *
	 * @param xmlCommand
	 * @param caller
	 * @param formClass
	 * @return
	 */
	public static ICtrlAction getCtrlAction(XMLCommand xmlCommand, IBaseItem caller, IForm formClass) {
		ICtrlAction ctrlAction = null;
		try {
			// 这里先临时处理一下文件名
			String[] names = xmlCommand.getPluginInfo().getBundleName().split("/");
			String fileName = names[names.length - 1];
			fileName = fileName.replaceAll(".dll", "");
			fileName = fileName.replaceAll(".jar", "");

			Class<?> ctrlActionClass = Application.getActiveApplication().getPluginManager()
					.getBundleClass(fileName.toLowerCase(), xmlCommand.getCtrlActionClass());

			if (ctrlActionClass != null) {
				Class[] paramTypes = {IBaseItem.class, IForm.class};
				Object[] params = {caller, formClass}; // 方法传入的参数
				Constructor<?> constructor = ctrlActionClass.getConstructor(paramTypes);
				ctrlAction = (ICtrlAction) constructor.newInstance(params);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			Application.getActiveApplication().getOutput().output(xmlCommand.getPluginInfo().getBundleName() + "&" + xmlCommand.getCtrlActionClass());
		}
		return ctrlAction;
	}
	//
	// #region Function_Public
	// /// <summary>
	// /// 读取XML节点构造CtrlAction
	// /// </summary>
	// public static ICtrlAction getCtrlAction(XmlNode itemNode, IBaseItem
	// caller, IForm formClass)
	// {
	// ICtrlAction ctrlAction = null;
	//
	// try
	// {
	// String className = _XMLTag.g_Empty;
	// String assemblyName = _XMLTag.g_Empty;
	// String codeType = _XMLTag.g_Empty;
	// try
	// {
	// className = itemNode.Attributes[_XMLTag.g_OnAction].Value;
	// }
	// catch { }
	// try
	// {
	// assemblyName =
	// itemNode.Attributes[_XMLTag.g_AttributionAssemblyName].Value;
	// }
	// catch { }
	// try
	// {
	// codeType = itemNode.Attributes[_XMLTag.g_AttributionCodeType].Value;
	// }
	// catch { }
	// String code = itemNode.InnerText;
	// if (className != _XMLTag.g_Empty)
	// {
	// ctrlAction = getCtrlAction(className, assemblyName, code, caller,
	// formClass, codeType);
	// }
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	//
	// return ctrlAction;
	// }
	//
	// /// <summary>
	// /// 根据具体参数构造CtrlAction
	// /// </summary>
	// public static ICtrlAction getCtrlAction(String className, String
	// assemblyName, String code, IBaseItem caller, IForm formClass, String
	// codeTypeString)
	// {
	// ICtrlAction ctrlAction = null;
	// try
	// {
	// if (className != null && className.Equals(_XMLTag.g_ScriptCodeFlag))
	// {
	//
	// //根据className属性确定是否为动态脚本
	// //String code = itemNode.InnerText;
	// CodeType codeType = CodeType.CSharp;
	// if (codeTypeString.toLowerCase().Equals(_XMLTag.g_ValueCodeType_VB))
	// {
	// codeType = CodeType.VB;
	// }
	// ctrlAction =
	// Application.getActiveApplication().Script.CompileCtrlActionCodeSnippet(codeType,
	// code);
	// }
	// else if (className != null &&
	// System.IO.File.Exists(CommonToolkit.PathWrap.getFullPathName(className)))
	// {
	//
	// String extension = System.IO.Path.getExtension(className);
	// CodeType codeType = CodeType.CSharp;
	// if (extension.toLowerCase().Equals(CoreResources.String_VBExtension))
	// {
	// codeType = CodeType.VB;
	// }
	// ctrlAction =
	// Application.getActiveApplication().Script.CompileCtrlActionClass(codeType,
	// new String[] { CommonToolkit.PathWrap.getFullPathName(className) });
	// }
	// else
	// {
	// //根据类名在指定动态库里生成对象
	// Assembly assembly = null;
	//
	// //查找一下是否已经加载进来了
	// Assembly[] assemblys = AppDomain.CurrentDomain.getAssemblies();
	// //非.net程序集的动态链接库没有元数据清单，因此虽然可以从当前AppDomain中知道它，但是无法读取
	// //其中的字段、引用等信息，会抛异常，需要过滤
	// foreach (Assembly a in assemblys)
	// {
	// if (!a.IsDynamic)
	// {
	// assembly = null;
	// if (assemblyName.Length != 0 &&
	// a.FullName.toLowerCase().IndexOf(assemblyName.toLowerCase()) == 0 ||
	// a.Location.toLowerCase().Equals(assemblyName.toLowerCase()))
	// {
	// assembly = a;
	// break;
	// }
	// }
	// }
	//
	// //如果没有加载，加载一下
	// if (assembly == null && assemblyName != null && assemblyName.Length
	// != 0)
	// {
	// assemblyName = CommonToolkit.PathWrap.getFullPathName(assemblyName);
	// assembly = System.Reflection.Assembly.LoadFrom(assemblyName);
	// }
	//
	// String exceptionMessage = String.Empty;
	// try
	// {
	// Type type = assembly.getType(className);
	// if (type != null)
	// {
	// ConstructorInfo constructor = type.getConstructor(new Type[] {
	// typeof(IBaseItem), typeof(IForm) });
	// if (constructor != null)
	// {
	// ctrlAction = constructor.Invoke(new object[] { caller, formClass })
	// as ICtrlAction;
	// }
	// }
	// }
	// catch (Exception ex)
	// {
	// exceptionMessage = ex.StackTrace;
	// }
	//
	// try
	// {
	// if (ctrlAction == null)
	// {
	// exceptionMessage = String.Empty;
	// ctrlAction = assembly.CreateInstance(className) as ICtrlAction;
	// }
	// }
	// catch (Exception ex)
	// {
	// exceptionMessage = ex.StackTrace;
	// }
	//
	// if (exceptionMessage.Length != 0)
	// {
	// Application.getActiveApplication().Output.Output(exceptionMessage,
	// InfoType.Exception);
	// }
	//
	// if (ctrlAction == null && Application.getActiveApplication().Output
	// != null)
	// {
	// Application.getActiveApplication().Output.Output("_CtrlActionNotImplemented");
	// }
	// }
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	// return ctrlAction;
	// }
	//
	// public static System.Windows.Forms.Keys getKeysByName(String name)
	// {
	// Keys result = Keys.None;
	// if
	// (name.toLowerCase().Equals(CoreResources.String_ShortCut_Ctrl.toLowerCase()))
	// {
	// result = Keys.Control;
	// }
	// else
	// {
	// foreach (Keys key in AllKeys)
	// {
	// if (key.ToString().Equals(name))
	// {
	// result = key;
	// break;
	// }
	// }
	// }
	//
	// return result;
	// }
	// #endregion
	//
	// #region Function_Event
	//
	// #endregion
	//
	// #region Function_Private
	//
	// #endregion
	//
	// #region Event
	//
	// #endregion
	//
	// #region InterfaceMembers
	//
	// #endregion
	//
	// #region NestedTypes
	//
	// #endregion
}
