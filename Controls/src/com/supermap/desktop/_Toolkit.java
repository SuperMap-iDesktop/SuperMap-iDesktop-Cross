package com.supermap.desktop;

public class _Toolkit {
  private _Toolkit(){
	  //  默认实现
  }
	
	//  此类将不再保留，请全部移植到 UICommonToolkit 里面
	
	
	
	
	
	
//	const String g_strDesktopPublicKeyToken = "7cd7dcc384397b6e";
//    const String g_strTestPublicKeyToken = "23d8e753f5ab9fa4";
//    #region Construct
//    static _Toolkit()
//    {
//        try
//        {
//            m_showSelectedLayerslegend = true;
//            //m_listCopyLayer3DsTreeNode = null;
//            //SuperMap.Desktop.CommonToolkit.AddFileToRecentFileEvent += new SuperMap.Desktop.CommonToolkit.AddFileToRecentFileEventHandle(_Toolkit_AddFileToRecentFileEvent);
//        }
//        catch (System.Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//    }
//    #endregion
//
//    #region Variant
//    public static int MaxRecordCount
//    {
//        get
//        {
//            return 2000;
//        }
//    }
//
//    private static boolean m_IsShowContextMenu = true;
//    public static boolean IsShowContextMenu
//    {
//        get { return m_IsShowContextMenu; }
//        set { m_IsShowContextMenu = value; }
//    }
//    #endregion
//
//    #region Event
//    #endregion
//
//    #region Function
//    public static void BindForms(IForm[] forms, boolean autoLayoutWindows, boolean isMergeForm, boolean isAddToCurrentGroups)
//    {
//        FormBase formBase = Application.ActiveApplication.MainForm as FormBase;
//        formBase.BindGroupManager.Clear();
//        //IBindGroup bindGroup = (Application.ActiveApplication.MainForm as FormBase).BindGroupManager[0];
//        if (forms == null || forms.Length < 2)
//        {
//            return;
//        }
//        List<IForm> relateForms = new List<IForm>();//待绑定的窗口List
//        relateForms.AddRange(forms);
//        BindGroup bindGroup = new BindGroup();
//        for (int i = 0; i < forms.Length; i++)
//        {
//            IFormTabular formTabular = forms[i] as IFormTabular;
//            IFormSuperTabular formSuperTabular = forms[i] as IFormSuperTabular;
//
//            if ((formTabular != null && formTabular.TabularControl.Recordset != null)
//                || (formSuperTabular != null && formSuperTabular.SuperTabular.Recordset != null))
//            {
//                Recordset formRecordset = formTabular != null ? formTabular.TabularControl.Recordset : formSuperTabular.SuperTabular.Recordset;
//                for (int j = 0; j < forms.Length; j++)
//                {
//                    IForm form = forms[j];
//                    if (form is IFormMap)
//                    {
//                        IFormMap formMap = form as IFormMap;
//                        Selection[] selections = formMap.MapControl.Map.FindSelection(true);
//                        if (selections != null && selections.Length > 0)
//                        {
//                            for (int k = 0; k < selections.Length; k++)
//                            {
//                                if (formRecordset.Dataset == selections[k].Dataset)
//                                {
//                                    Recordset recordset = selections[k].ToRecordset();
//                                    try
//                                    {
//                                        List<int> ids = new List<int>();
//                                        while (!recordset.IsEOF)
//                                        {
//                                            ids.Add(recordset.GetID());
//                                            recordset.MoveNext();
//                                        }
//                                        if (formTabular != null)
//                                        {
//                                            formTabular.TabularControl.SelectionIDs = ids.ToArray();
//                                        }
//                                        else
//                                        {
//                                            formSuperTabular.SuperTabular.SelectedIDs = ids.ToArray();
//                                        }
//                                    }
//                                    finally
//                                    {
//                                        CommonToolkit.ReleaseRecordset(ref recordset);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                        break;
//                    }
//                    else if (form is IFormScene)
//                    {
//                        IFormScene formScene = form as IFormScene;
//                        Selection3D[] selection3Ds = formScene.SceneControl.Scene.FindSelection(true);
//                        if (selection3Ds != null && selection3Ds.Length > 0)
//                        {
//                            for (int k = 0; k < selection3Ds.Length; k++)
//                            {
//                                Layer3DDataset layer = selection3Ds[k].Layer as Layer3DDataset;
//                                if (layer != null && formRecordset.Dataset == layer.Dataset)
//                                {
//                                    Recordset recordset = selection3Ds[k].ToRecordset();
//                                    try
//                                    {
//                                        List<int> ids = new List<int>();
//                                        while (!recordset.IsEOF)
//                                        {
//                                            ids.Add(recordset.GetID());
//                                            recordset.MoveNext();
//                                        }
//
//                                        if (formTabular != null)
//                                        {
//                                            formTabular.TabularControl.SelectionIDs = ids.ToArray();
//                                        }
//                                        else
//                                        {
//                                            formSuperTabular.SuperTabular.SelectedIDs = ids.ToArray();
//                                        }
//                                    }
//                                    finally
//                                    {
//                                        CommonToolkit.ReleaseRecordset(ref recordset);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//
//        foreach (IForm form in relateForms)
//        {
//            bindGroup.Add(form);
//        }
//        formBase.BindGroupManager.Add(bindGroup);
//
//        if (autoLayoutWindows)
//        {
//            LayoutBindGroupForms(bindGroup, isMergeForm, isAddToCurrentGroups);
//        }
//        for (int i = 0; i < bindGroup.FormMaps.Length; i++)
//        {
//            bindGroup.FormMaps[i].MapControl.Map.RefreshEx(bindGroup.FormMaps[i].MapControl.Map.ViewBounds);
//        }
//    }
//
//    public static void LayoutBindGroupForms(BindGroup bindGroup, boolean isMergeForm, boolean isAddToCurrentGroups)
//    {
//        try
//        {
//            if (bindGroup != null)
//            {
//                IFormMain mainForm = Application.ActiveApplication.MainForm;
//                List<IForm> formList = new List<IForm>();//待绑定的窗口排序后List
//                formList.AddRange(bindGroup.FormMaps);
//                formList.AddRange(bindGroup.FormScenes);
//                formList.AddRange(bindGroup.FormTabulars);
//                formList.AddRange(bindGroup.FormSuperTabulars);
//
//                if (mainForm.MdiFormType == MdiFormType.Type_Tab)
//                {
//                    List<IForm> unRelateForms = new List<IForm>();
//                    for (int i = 0; i < Application.ActiveApplication.MainForm.FormManager.Count; i++)
//                    {
//                        IForm form = Application.ActiveApplication.MainForm.FormManager[i];
//                        if (!formList.Contains(form))
//                        {
//                            unRelateForms.Add(form);
//                        }
//                    }
//                    foreach (IForm form in unRelateForms)
//                    {
//                        IMdiChildTab tab = mainForm.MdiTabGroupManager.FindMdiTab(form as Form);
//                        tab.MoveTo(mainForm.MdiTabGroupManager[0]);
//                    }
//                    if (isMergeForm)
//                    {
//                        List<IMdiChildTab> mapTabs = new List<IMdiChildTab>();
//                        List<IMdiChildTab> sceneTabs = new List<IMdiChildTab>();
//                        List<IMdiChildTab> tabularTabs = new List<IMdiChildTab>();
//                        List<IMdiChildTab> superTabularTabs = new List<IMdiChildTab>();
//                        for (int i = 0; i < formList.Count; i++)
//                        {
//                            Form form = formList[i] as Form;
//                            IMdiChildTab tab = mainForm.MdiTabGroupManager.FindMdiTab(form);
//                            if (formList[i] is IFormMap)
//                            {
//                                mapTabs.Add(tab);
//                            }
//                            else if (formList[i] is IFormScene)
//                            {
//                                sceneTabs.Add(tab);
//                            }
//                            else if (formList[i] is IFormTabular)
//                            {
//                                tabularTabs.Add(tab);
//                            }
//                            else if (formList[i] is IFormSuperTabular)
//                            {
//                                superTabularTabs.Add(tab);
//                            }
//                        }
//                        List<List<IMdiChildTab>> tabGroups = new List<List<IMdiChildTab>>();
//                        if (mapTabs.Count > 0)
//                        {
//                            tabGroups.Add(mapTabs);
//                        }
//                        if (sceneTabs.Count > 0)
//                        {
//                            tabGroups.Add(sceneTabs);
//                        }
//                        if (tabularTabs.Count > 0)
//                        {
//                            tabGroups.Add(tabularTabs);
//                        }
//                        if (superTabularTabs.Count > 0)
//                        {
//                            tabGroups.Add(superTabularTabs);
//                        }
//                        if (!isAddToCurrentGroups)//如果不添加到当前标签组中，那么就依次将每个组输出为一个新的标签组中
//                        {
//                            for (int i = 0; i < tabGroups.Count; i++)
//                            {
//                                mainForm.FormManager.ActiveForm = tabGroups[i][0].Form as IForm;
//                                mainForm.MdiTabGroupManager.NewMdiTabGroup();
//                                IMdiTabGroup group = tabGroups[i][0].Group;
//                                for (int j = 1; j < tabGroups[i].Count; j++)
//                                {
//                                    tabGroups[i][j].MoveTo(group);
//                                }
//                            }
//                        }
//                        else
//                        {
//                            int currentGroupCount = mainForm.MdiTabGroupManager.Count;
//                            int availableGroupCount = Math.Min(tabGroups.Count, mainForm.MdiTabGroupManager.Count);
//                            for (int i = 0; i < availableGroupCount; i++)
//                            {
//                                for (int j = 0; j < tabGroups[i].Count; j++)
//                                {
//                                    IMdiChildTab tab = tabGroups[i][j];
//                                    tab.MoveTo(mainForm.MdiTabGroupManager[i]);
//                                }
//                            }
//                            if (tabGroups.Count > currentGroupCount)
//                            {
//                                for (int i = currentGroupCount; i < tabGroups.Count; i++)
//                                {
//                                    mainForm.FormManager.ActiveForm = tabGroups[i][0].Form as IForm;
//                                    mainForm.MdiTabGroupManager.NewMdiTabGroup();
//                                    IMdiTabGroup group = tabGroups[i][0].Group;
//                                    for (int j = 1; j < tabGroups[i].Count; j++)
//                                    {
//                                        tabGroups[i][j].MoveTo(group);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else
//                    {
//                        if (!isAddToCurrentGroups)//如果不添加到当前标签组中，那么就依次将窗口输出为一个新的标签组中
//                        {
//                            for (int i = 0; i < formList.Count; i++)
//                            {
//                                IForm form = formList[i];
//                                mainForm.FormManager.ActiveForm = form;
//                                mainForm.MdiTabGroupManager.NewMdiTabGroup();
//                            }
//                        }
//                        else
//                        {
//                            int currentGroupCount = mainForm.MdiTabGroupManager.Count;
//                            int availableGroupCount = Math.Min(formList.Count, currentGroupCount);
//                            for (int i = 0; i < availableGroupCount; i++)
//                            {
//                                Form form = formList[i] as Form;
//                                if (form != null)
//                                {
//                                    IMdiChildTab tab = mainForm.MdiTabGroupManager.FindMdiTab(form);
//                                    if (tab != null)//理论上说，tab必定不为null，因为窗体既然已经存在，就必定能找到它对应的tab。
//                                    {
//                                        tab.MoveTo(mainForm.MdiTabGroupManager[i]);
//                                    }
//                                    else
//                                    {
//                                        mainForm.FormManager.ActiveForm = formList[i];
//                                        mainForm.MdiTabGroupManager.NewMdiTabGroup();
//                                    }
//                                }
//                            }
//                            if (formList.Count > currentGroupCount)
//                            {
//                                for (int i = currentGroupCount; i < formList.Count; i++)
//                                {
//                                    mainForm.FormManager.ActiveForm = formList[i];
//                                    mainForm.MdiTabGroupManager.NewMdiTabGroup();
//                                }
//                            }
//
//                        }
//                    }
//
//                    for (int i = 0; i < formList.Count; i++)
//                    {
//                        (formList[i] as Form).WindowState = FormWindowState.Normal;
//                    }
//                }
//                else
//                {
//                    //if (mainForm.MdiFormType != MdiFormType.Type_Window)
//                    //{
//                    //mainForm.MdiFormType = MdiFormType.Type_Window;
//                    //}
//                    Form mainFormObj = mainForm as Form;
//                    List<Form> windows = new List<Form>();
//                    foreach (IFormMap formMap in bindGroup.FormMaps)
//                    {
//                        windows.Add(formMap as Form);
//                    }
//                    foreach (IFormScene formScene in bindGroup.FormScenes)
//                    {
//                        windows.Add(formScene as Form);
//                    }
//                    foreach (IFormTabular formTabular in bindGroup.FormTabulars)
//                    {
//                        windows.Add(formTabular as Form);
//                    }
//                    foreach (IFormSuperTabular formSuperTabular in bindGroup.FormSuperTabulars)
//                    {
//                        windows.Add(formSuperTabular as Form);
//                    }
//                    List<Form> unRelateForms = new List<Form>();
//                    Form formObj = null;
//                    int minimizedFormRows = 0;
//                    int minimizedFormHeight = 0;
//                    int minimizedFormWidth = 0;
//                    for (int i = 0; i < Application.ActiveApplication.MainForm.FormManager.Count; i++)
//                    {
//                        formObj = Application.ActiveApplication.MainForm.FormManager[i] as Form;
//                        if (!windows.Contains(formObj))
//                        {
//                            if (formObj.WindowState != FormWindowState.Minimized)
//                            {
//                                formObj.WindowState = FormWindowState.Minimized;
//                            }
//                            unRelateForms.Add(formObj);
//                        }
//                        else
//                        {
//                            if (formObj.WindowState != FormWindowState.Normal)
//                            {
//                                formObj.WindowState = FormWindowState.Normal;
//                            }
//                        }
//                    }
//                    Rectangle rect = mainFormObj.ClientRectangle;
//                    if (mainForm.MdiFormType == MdiFormType.Type_Window)
//                    {
//                        foreach (Control control in mainFormObj.Controls)
//                        {
//                            if (control is MdiClient)
//                            {
//                                (control as MdiClient).LayoutMdi(MdiLayout.TileHorizontal);//这里需要重新布局一下，因为最小化的窗口有可能被用户挪到不合适的地方了。
//                                rect = control.ClientRectangle;//这里用clientRectangle取得坐标，然后下面再取实际的宽高，因为clientRectangle是不包括滑动条的。
//                                rect.Height = control.Bounds.Height;
//                                rect.Width = control.Bounds.Width;
//                                break;
//                            }
//                        }
//                        //最小化的窗口所占用的客户区大小目前没发现有提供的入口可以用，就手动算吧。
//                        if (unRelateForms.Count > 0)
//                        {
//                            minimizedFormHeight = unRelateForms[0].Height;
//                            minimizedFormWidth = unRelateForms[0].Width;
//                            int rowChildCount = (rect.Width / minimizedFormWidth);
//                            minimizedFormRows = (unRelateForms.Count / rowChildCount) + ((unRelateForms.Count % rowChildCount > 0) ? 1 : 0);
//                            rect.Height = Math.Max(rect.Height - minimizedFormRows * minimizedFormHeight, 1);//以防高度小于0
//                        }
//                    }
//                    LayoutMdiWindowTypeForms(rect, windows);
//                }
//                mainForm.FormManager.ActiveForm = formList[0];
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//    }
//
//    private static void LayoutMdiWindowTypeForms(Rectangle rect, List<Form> forms)
//    {
//        try
//        {
//            switch (forms.Count)
//            {
//                case 0:
//                case 1:
//                    break;
//                case 2:
//                    {
//                        forms[0].Location = new Point(rect.Left, rect.Top);
//                        forms[1].Location = new Point(rect.Left + rect.Width / 2, rect.Top);
//                        forms[0].Width = forms[1].Width = rect.Width / 2;
//                        forms[0].Height = forms[1].Height = rect.Height;
//                    }
//                    break;
//                case 3:
//                    {
//                        forms[0].Location = new Point(rect.Left, rect.Top);
//                        forms[1].Location = new Point(rect.Left + rect.Width / 2, rect.Top);
//                        forms[0].Width = forms[1].Width = rect.Width / 2;
//                        int height = rect.Height * 2 / 3;
//                        forms[0].Height = forms[1].Height = height;
//                        forms[2].Location = new Point(rect.Left, rect.Top + height);
//                        forms[2].Width = rect.Width;
//                        forms[2].Height = rect.Height / 3;
//                    }
//                    break;
//                case 4:
//                    {
//                        int height = rect.Height / 2;
//                        int width = rect.Width / 2;
//                        forms[0].Location = new Point(rect.Left, rect.Top);
//                        forms[1].Location = new Point(rect.Left + width, rect.Top);
//                        forms[0].Width = forms[1].Width = width;
//                        forms[0].Height = forms[1].Height = height;
//
//                        forms[2].Location = new Point(rect.Left, rect.Top + height);
//                        forms[3].Location = new Point(rect.Left + width, rect.Top + height);
//                        forms[2].Width = forms[3].Width = width;
//                        forms[2].Height = forms[3].Height = height;
//                    }
//                    break;
//                default:
//                    {
//                        int height = rect.Height / forms.Count;
//                        for (int i = 0; i < forms.Count; i++)
//                        {
//                            forms[i].Location = new Point(rect.Left, rect.Top + height * i);
//                            forms[i].Width = rect.Width;
//                            forms[i].Height = height;
//                        }
//                    }
//                    break;
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//    }
//
//    //public enum OpenDatasourceResult
//    //{
//    //    Successed = 0,
//    //    Failed_UnKnow,
//    //    Failed_License_Wrong,
//    //    Failed_File_Wrong,
//    //    Failed_Password_Wrong,
//    //    Failed_Version_Wrong
//    //}
//
//    ////处理文件拖放操作，现在可以支持工作空间、数据源、文件夹
//    ////smw、sxw、smwu、sxwu
//    ////udb、udd
//    ////tiff、sit、bmp、jpg...
//    public static void OpenWorkEnvironment(String filePath, String passWord, boolean isReadOnly = false)
//    {
//        FunctionID funID = FunctionID.None;
//        try
//        {
//            String strFileExtension = Path.GetExtension(filePath).ToLower();
//            if (strFileExtension.Equals(".smw") ||
//                strFileExtension.Equals(".sxw") ||
//                strFileExtension.Equals(".smwu") ||
//                strFileExtension.Equals(".sxwu"))
//            {
//                (Application.ActiveApplication.MainForm as Form).Activate();
//
//                WorkspaceConnectionInfo wsinfo = new WorkspaceConnectionInfo();
//                wsinfo.Server = filePath;
//                wsinfo.Password = passWord;
//                wsinfo.Type = CommonToolkit.WorkspaceTypeWrap.GetWorkspaceTypeByFileExt(Path.GetExtension(filePath));
//
//                funID = FunctionID.OpenFileWorkspace;
//                Application.ActiveApplication.UserInfoManage.FunctionRun(funID);
//                OpenWorkspaceResult result = OpenWorkspace(wsinfo, true);
//                if (result == OpenWorkspaceResult.Successed)
//                {
//                    if (Application.ActiveApplication.MainForm.FormManager.Count > 0)
//                    {
//                        Application.ActiveApplication.MainForm.FormManager.CloseAll();
//                    }
//                }
//                else while (result == OpenWorkspaceResult.Failed_Password_Wrong)
//                    {
//                        String inputPassword = FormInput.ShowInputBox(Properties.CoreResources.String_Prompt, Properties.CoreResources.String_WorkspacePasswordPrompt, true);
//                        if (inputPassword != null)//按取消按钮不执行打开工作空间的操作。
//                        {
//                            wsinfo.Password = inputPassword;
//                            result = OpenWorkspace(wsinfo, false);
//                        }
//                        else
//                        {
//                            result = OpenWorkspaceResult.Failed_Cancel;
//                        }
//                    }
//
//                if (result != OpenWorkspaceResult.Successed)
//                {
//                    try
//                    {
//                        if (result != OpenWorkspaceResult.Failed_Cancel)
//                        {
//                            //如果用户不是取消保存当前工作空间，先用close一下当前的，确保还原到默认的工作空间。Note by tangzhq,2013-3-18
//                            Application.ActiveApplication.Workspace.Close();
//                        }
//                    }
//                    catch (Exception ex)
//                    { Application.ActiveApplication.Output.Output(ex, InfoType.Exception, funID); }
//                    String stMsg = String.Format(Properties.CoreResources.String_OpenWorkspaceFailed, filePath);
//                    if (result == OpenWorkspaceResult.Failed_Password_Wrong)
//                    {
//                        stMsg = String.Format(ControlsResources.String_OpenWorkspaceFailed_WrongPassword, filePath);
//                    }
//                    Application.ActiveApplication.Output.Output(stMsg);
//                }
//            }
//            else
//            {
//                Datasource datasource = null;
//                //是目录
//                if (Directory.Exists(filePath))
//                {
//                    //得到所有支持的文件
//                    String[] fileNames = Directory.GetFiles(filePath, "*.*", SearchOption.AllDirectories);
//                    foreach (String fileName in fileNames)
//                    {
//                        if (CommonToolkit.DatasourceWrap.IsSuportedFileEngineExt(Path.GetExtension(fileName)))
//                        {
//                            boolean passwordWorry = false;
//                            funID = FunctionID.OpenFileDatasource;
//                            Application.ActiveApplication.UserInfoManage.FunctionRun(funID);
//                            datasource = OpenFileDatasource(fileName, passWord, false, true, out passwordWorry);
//                            if (datasource == null)
//                            {
//                                boolean isCancel = false;
//                                while (datasource == null && passwordWorry && !isCancel)
//                                {
//                                    String strInfo = String.Format(UI.Properties.ControlsResources.String_InputDatasourcePassword, Path.GetFileName(fileName));
//                                    passWord = FormInput.ShowInputBox(Properties.CoreResources.String_Prompt, strInfo, true);
//                                    if (passWord == null)
//                                    {
//                                        isCancel = true;
//                                    }
//                                    else
//                                    {
//                                        datasource = OpenFileDatasource(fileName, passWord, false, false, out passwordWorry);
//                                    }
//                                }
//
//                                String strMsg = String.Empty;
//                                if (datasource == null)
//                                {
//                                    strMsg = String.Format(UI.Properties.ControlsResources.String_OpenDatasourceFailed, fileName);
//                                    Application.ActiveApplication.Output.Output(strMsg);
//                                }
//                            }
//                        }
//                    }
//                }
//                else
//                {
//                    if (CommonToolkit.DatasourceWrap.IsSuportedFileEngineExt(Path.GetExtension(filePath)))
//                    {
//                        Workspace workspace = Application.ActiveApplication.Workspace;
//
//                        boolean hasOpen = false;
//                        foreach (Datasource other in workspace.Datasources)
//                        {
//                            if (other.ConnectionInfo.Server.Equals(filePath))
//                            {
//                                hasOpen = true;
//                                break;
//                            }
//                        }
//                        if (!hasOpen)
//                        {
//                            boolean passwordWorry = false;
//                            funID = FunctionID.OpenFileDatasource;
//                            Application.ActiveApplication.UserInfoManage.FunctionRun(funID);
//                            datasource = OpenFileDatasource(filePath, passWord, false, true, out passwordWorry);
//                            if (datasource == null)
//                            {
//                                boolean isCancel = false;
//                                while (datasource == null && passwordWorry && !isCancel)
//                                {
//                                    String strInfo = String.Format(UI.Properties.ControlsResources.String_InputDatasourcePassword, Path.GetFileName(filePath));
//                                    passWord = FormInput.ShowInputBox(Properties.CoreResources.String_Prompt, strInfo, true);
//                                    if (passWord == null)
//                                    {
//                                        isCancel = true;
//                                    }
//                                    else
//                                    {
//                                        datasource = OpenFileDatasource(filePath, passWord, isReadOnly, false, out passwordWorry);
//                                    }
//                                }
//
//                                String strMsg = String.Empty;
//                                if (datasource == null)
//                                {
//                                    strMsg = String.Format(UI.Properties.ControlsResources.String_OpenDatasourceFailed, filePath);
//                                    Application.ActiveApplication.Output.Output(strMsg);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, funID);
//        }
//    }
//
//    // 打开所有类型的工作空间 [11/30/2011 zhoujt]
//    // 建议其他地方在调用的时候直接构造WorkspaceConnectionInfo 调用该方法。
//    public static OpenWorkspaceResult OpenWorkspace(WorkspaceConnectionInfo info, boolean first)
//    {
//        OpenWorkspaceResult result = OpenWorkspaceResult.Failed_Password_Wrong;
//        try
//        {
//            // 打开新的工作空间之前需要先关闭当前工作空间 [11/30/2011 zhoujt]
//            if (CommonToolkit.WorkspaceWrap.CloseWorkspace())
//            {
//                Application.ActiveApplication.UserInfoManage.FunctionStart(FunctionID.OpenFileWorkspace);
//                System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.WaitCursor;
//                Toolkit.ClearErrors();
//                boolean bOpened = false;
//                Dictionary<String, String> readOnlyDatasourceDictionary = new Dictionary<String, String>();
//                try
//                {
//                    //如果数据源中含有只读文件，则给出提示是否已只读打开。
//                    //先构造一个工作空间，判断一下是否需要进行提示（这个主要是处理UI问题，因为数据源只有在打开工作空间的情况下才能够判断
//                    //如果直接在application下的工作空间下判断，界面上就会出现 打开失败- 提示是否只读打开-重新打开的现象，体验不好）
//                    Workspace workspace = new Workspace();
//                    bOpened = workspace.Open(info);
//
//                    if (bOpened && (workspace.Type == WorkspaceType.SMW
//                        || workspace.Type == WorkspaceType.SMWU
//                        || workspace.Type == WorkspaceType.SXW
//                        || workspace.Type == WorkspaceType.SXWU))
//                    {
//                        FormOpenReadOnlyDatasourceMessageBox messageBox = null;
//                        boolean isAllExcuteThisOperation = false;
//                        foreach (Datasource datasource in workspace.Datasources)
//                        {
//                            //如果上一次是以只读打开形式打开的文件，则保存工作空间后可以成功打开，故不需要给出提示。
//                            //所以如果数据源没有成功打开，且文件类型是只读类型，则给出提示。
//                            if (!datasource.IsOpened)
//                            {
//                                String datasourceFilePath = datasource.ConnectionInfo.Server;
//                                if (!datasourceFilePath.Contains(":memory:"))
//                                {
//                                    FileInfo fileInfo = new FileInfo(datasourceFilePath);
//                                    //仅限于文件型工作空间与文件型数据源，sdb已淘汰，不进行判断
//                                    if (fileInfo.Extension.Equals(".udb"))
//                                    {
//                                        String datasourceUddPath = datasource.ConnectionInfo.Server.Replace("udb", "udd");
//                                        if (File.Exists(datasourceFilePath) && File.Exists(datasourceUddPath))
//                                        {
//                                            FileInfo uddFileInfo = new FileInfo(datasourceUddPath);
//                                            if (fileInfo.IsReadOnly || uddFileInfo.IsReadOnly)
//                                            {
//                                                String message = String.Format(Properties.CoreResources.String_OpenReadOnlyDatasourceWarning, datasourceFilePath);
//                                                if (!isAllExcuteThisOperation)
//                                                {
//                                                    if (messageBox == null)
//                                                    {
//                                                        messageBox = new FormOpenReadOnlyDatasourceMessageBox(message, workspace.Datasources.Count > 1);
//                                                    }
//                                                    else
//                                                    {
//                                                        messageBox.Message = message;
//                                                    }
//                                                    DialogResult dialogResult = messageBox.ShowDialog();
//                                                    if (dialogResult == DialogResult.Yes)
//                                                    {
//                                                        readOnlyDatasourceDictionary.Add(datasourceFilePath, datasource.Alias);
//                                                        if (messageBox.AllDatasourcesExcuteThisOperation)
//                                                        {
//                                                            isAllExcuteThisOperation = true;
//                                                        }
//                                                    }
//                                                }
//                                                else if (isAllExcuteThisOperation)
//                                                {
//                                                    readOnlyDatasourceDictionary.Add(datasourceFilePath, datasource.Alias);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    workspace.Close();
//                    workspace.Dispose();
//                    //真正的打开Application下面的工作空间
//                    bOpened = Application.ActiveApplication.Workspace.Open(info);
//                    if (bOpened)
//                    {
//                        result = OpenWorkspaceResult.Successed;
//                        if (readOnlyDatasourceDictionary != null && readOnlyDatasourceDictionary.Count > 0)
//                        {
//                            foreach (KeyValuePair<String, String> item in readOnlyDatasourceDictionary)
//                            {
//                                Application.ActiveApplication.Workspace.Datasources.Close(item.Value);
//                                boolean isPasswordWorry = false;
//                                Datasource datasource = _Toolkit.OpenFileDatasource(item.Key, String.Empty, true, true, out isPasswordWorry);
//                            }
//                        }
//                        for (int index = Application.ActiveApplication.Workspace.Datasources.Count - 1; index >= 0; index--)
//                        {
//                            Datasource datasource = Application.ActiveApplication.Workspace.Datasources[index];
//                            if (!datasource.IsOpened)
//                            {
//                                if (!datasource.ConnectionInfo.Server.Contains(":memory:"))
//                                {
//                                    String failedInfo = String.Format(SuperMap.Desktop.Properties.Resources.String_Message_DataSource_Openfail, datasource.Alias);
//                                    Application.ActiveApplication.Output.Output(failedInfo);
//                                }
//                                else
//                                {
//                                    Application.ActiveApplication.Workspace.Datasources.Close(datasource.Alias);
//                                }
//                            }
//                        }
//                    }
//                    else
//                    {
//                        ErrorInfo[] errorInfos = Toolkit.GetLastErrors(5);
//                        for (int i = 0; i < errorInfos.Length; i++)
//                        {
//                            if (errorInfos[i].Marker.Equals(Properties.CoreResources.String_UGS_PASSWORD))
//                            {
//                                if (!first)
//                                {
//                                    Application.ActiveApplication.Output.Output(errorInfos[i].Message, InfoType.Information);
//                                }
//                                result = OpenWorkspaceResult.Failed_Password_Wrong;
//                                break;
//                            }
//                            else
//                            {
//                                Application.ActiveApplication.Output.Output(errorInfos[i].Message, InfoType.Information);
//
//                                result = OpenWorkspaceResult.Failed_UnKnow;
//                            }
//                        }
//                    }
//                }
//                catch (System.Exception ex)
//                {
//                    if (ex.Message.Equals("supermap_license_error_wronglicensedata", StringComparison.OrdinalIgnoreCase))
//                    {
//                        Application.ActiveApplication.Output.Output(SuperMap.Desktop.Properties.CoreResources.String_Wronglicensedata);
//                    }
//                    else
//                    {
//                        ErrorInfo[] errorInfos = Toolkit.GetLastErrors(5);
//                        for (int i = 0; i < errorInfos.Length; i++)
//                        {
//                            if (errorInfos[i].Marker.Equals(Properties.CoreResources.String_UGS_PASSWORD))
//                            {
//                                if (!first)
//                                {
//                                    Application.ActiveApplication.Output.Output(errorInfos[i].Message, InfoType.Information);
//                                }
//                                result = OpenWorkspaceResult.Failed_Password_Wrong;
//                                break;
//                            }
//                            else
//                            {
//                                Application.ActiveApplication.Output.Output(errorInfos[i].Message, InfoType.Information);
//
//                                result = OpenWorkspaceResult.Failed_UnKnow;
//                            }
//                        }
//                    }
//                    if (result != OpenWorkspaceResult.Failed_Password_Wrong)
//                    {
//                        Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.OpenFileWorkspace);
//                    }
//                }
//
//                switch (info.Type)
//                {
//                    case WorkspaceType.SMW:
//                    case WorkspaceType.SMWU:
//                    case WorkspaceType.SXWU:
//                    case WorkspaceType.SXW:
//                        {
//                            // 如果是文件型工作空间并且打开成功了，则需要添加到最近文件列表中
//                            if (result == OpenWorkspaceResult.Successed)
//                            {
//                                AddWorkspaceFileToRecentFile(info.Server, FunctionID.OpenFileWorkspace);
//                            }
//                        }
//                        break;
//                    default:
//                        {
//                        }
//                        break;
//                }
//            }
//            else
//            {
//                result = OpenWorkspaceResult.Failed_Cancel;
//            }
//
//            if (result == OpenWorkspaceResult.Successed)
//            {
//                Application.ActiveApplication.UserInfoManage.FunctionSuccess(FunctionID.OpenFileWorkspace);
//            }
//            else
//            {
//                Application.ActiveApplication.UserInfoManage.FunctionFailed(FunctionID.OpenFileWorkspace);
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.OpenFileWorkspace);
//        }
//        finally
//        {
//            System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.Default;
//        }
//
//        return result;
//    }
//
//    //将指定路径的工作空间添加到最近文件列表
//    public static boolean AddWorkspaceFileToRecentFile(String filePath, FunctionID funID)
//    {
//        boolean result = false;
//
//        try
//        {
//            IRecentFileGroup fileGroup = Application.ActiveApplication.MainForm.RecentFileManager[SuperMap.Desktop.Properties.CoreResources.String_RecentWorkspace];
//
//            filePath = filePath.Replace(Path.DirectorySeparatorChar, Path.AltDirectorySeparatorChar);
//            //如果文件列表中里面有当前文件，则删除该条记录
//            fileGroup.Remove(filePath);
//
//            if (fileGroup.Count > 0)
//            {
//                fileGroup.Insert(0, filePath);
//            }
//            else
//            {
//                fileGroup.Add(filePath);
//            }
//
//            result = true;
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, funID);
//        }
//
//        return result;
//    }
//
//    //打开指定路径与密码的文件型数据源
//    public static Datasource OpenFileDatasource(String fileName, String passWord, boolean isReadOnly)
//    {
//        Datasource datasource = null;
//
//        Workspace workspace = Application.ActiveApplication.Workspace;
//        try
//        {
//            System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.WaitCursor;
//            DatasourceConnectionInfo info = new DatasourceConnectionInfo();
//
//            //看看该文件是否已经打开了
//            boolean alreadyOpen = false;
//            String strServer = String.Empty;
//            String strAlias = Path.GetFileNameWithoutExtension(fileName);
//            String strFileNameTemp = fileName.ToLower();
//            strFileNameTemp = strFileNameTemp.Replace(Path.AltDirectorySeparatorChar, Path.DirectorySeparatorChar);
//            for (int index = 0; index < workspace.Datasources.Count; index++)
//            {
//                strServer = workspace.Datasources[index].ConnectionInfo.Server.ToLower();
//                strServer = strServer.Replace(Path.AltDirectorySeparatorChar, Path.DirectorySeparatorChar);
//                if (strServer.CompareTo(strFileNameTemp) == 0 ||
//                    strServer.Replace(".udd", ".udb").CompareTo(strFileNameTemp) == 0 ||
//                    strServer.Replace(".udb", ".udd").CompareTo(strFileNameTemp) == 0)
//                {
//                    datasource = workspace.Datasources[index];
//                    alreadyOpen = true;
//                    break;
//                }
//            }
//
//            if (!alreadyOpen)
//            {
//                //找一个合适的别名
//                int index = 1;
//                while (workspace.Datasources.Contains(strAlias))
//                {
//                    strAlias = Path.GetFileNameWithoutExtension(fileName) + "_" + index.ToString();
//                    index++;
//                }
//
//                info.Alias = strAlias;
//                info.Server = fileName;
//                info.Password = passWord;
//                info.EngineType = CommonToolkit.EngineTypeWrap.GetEngineType(Path.GetExtension(fileName));
//
//                FileInfo fileInfo = new FileInfo(fileName);
//                info.IsReadOnly = fileInfo.IsReadOnly || isReadOnly || info.EngineType == EngineType.VectorFile;
//
//                try
//                {
//                    Application.ActiveApplication.UserInfoManage.FunctionStart(FunctionID.OpenFileDatasource);
//                    datasource = workspace.Datasources.Open(info);
//                    if (datasource != null)
//                    {
//                        // 数据源打开成功后将其添加到最近文件列表中 [12/12/2011 zhoujt]
//                        AddDatasourceToRecentFile(datasource, FunctionID.OpenFileDatasource);
//                    }
//                }
//                catch (System.Exception ex)
//                {
//                    if (ex.Message.Equals("supermap_license_error_wronglicensedata", StringComparison.OrdinalIgnoreCase))
//                    {
//                        Application.ActiveApplication.Output.Output(Properties.CoreResources.String_Wronglicensedata);
//                    }
//                    //Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.OpenFileDatasource);
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.OpenFileDatasource);
//        }
//        finally
//        {
//            System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.Default;
//        }
//
//        return datasource;
//    }
//
//    public static Datasource OpenFileDatasource(String fileName, String passWord, boolean isReadOnly, boolean first, out boolean passwordWorry)
//    {
//        Datasource datasource = null;
//        passwordWorry = false;
//        try
//        {
//            System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.WaitCursor;
//            Workspace workspace = Application.ActiveApplication.Workspace;
//            Toolkit.ClearErrors();
//            datasource = OpenFileDatasource(fileName, passWord, isReadOnly);
//            if (datasource == null)
//            {
//                ErrorInfo[] errorInfos = Toolkit.GetLastErrors(5);
//
//                // 首先判断一下是不是已知的错误，目前已知的就是密码错误
//                for (int i = 0; i < errorInfos.Length; i++)
//                {
//                    if (errorInfos[i].Marker.Equals(Properties.CoreResources.String_UGS_PASSWORD)
//                        || errorInfos[i].Marker.Equals(Properties.CoreResources.String_UGS_PASSWORDError))
//                    {
//                        passwordWorry = true;
//                        break;
//                    }
//                }
//
//                // 不是第一次打开，输出错误信息
//                if (!first)
//                {
//                    if (passwordWorry)
//                    {
//                        Application.ActiveApplication.Output.Output(SuperMap.Desktop.UI.Properties.ControlsResources.String_CurrentPasswordWrong);
//                    }
//                    else
//                    {
//                        for (int i = 0; i < errorInfos.Length; i++)
//                        {
//                            Application.ActiveApplication.Output.Output(errorInfos[i].Message);
//                        }
//                    }
//                }
//                Application.ActiveApplication.UserInfoManage.FunctionFailed(FunctionID.OpenFileDatasource);
//            }
//            else
//            {
//                Application.ActiveApplication.UserInfoManage.FunctionSuccess(FunctionID.OpenFileDatasource);
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.OpenFileDatasource);
//        }
//        finally
//        {
//            System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.Default;
//        }
//
//        return datasource;
//    }
//
//    // 关闭相应的数据源
//    public static void CloseDatasources(Datasource[] datasources)
//    {
//        try
//        {
//            String strdatasourceAlias = String.Empty;
//            String message = String.Empty;
//            if (datasources.Length == 1)
//            {
//                strdatasourceAlias = datasources[0].Alias;
//            }
//            else
//            {
//                for (int i = 0; i < datasources.Length; i++)
//                {
//                    if (i == datasources.Length - 1)
//                    {
//                        strdatasourceAlias += Properties.CoreResources.String_QuotationMarkHead + datasources[i].Alias + Properties.CoreResources.String_QuotationMarkEnd;
//                    }
//                    else
//                    {
//                        strdatasourceAlias += Properties.CoreResources.String_QuotationMarkHead + datasources[i].Alias + Properties.CoreResources.String_QuotationMarkEnd + Properties.CoreResources.String_IntervalMark;
//                    }
//                }
//            }
//            Application.ActiveApplication.UserInfoManage.FunctionRun(FunctionID.CloseDatasource);//加入用户体验计划
//            message = String.Format(Properties.CoreResources.String_MessageBox_CloseDatasource, strdatasourceAlias);
//
//            if (MessageBox.Show(message, UI.CommonToolkit.GetMessageBoxTitle(), MessageBoxButtons.OKCancel, MessageBoxIcon.Information) == DialogResult.OK)
//            {
//                for (int i = datasources.Length - 1; i >= 0; i--)
//                {
//                    Application.ActiveApplication.UserInfoManage.FunctionStart(FunctionID.CloseDatasource);
//                    try
//                    {
//                        boolean result = Application.ActiveApplication.Workspace.Datasources.Close(datasources[i].Alias);
//
//                        if (result)
//                        {
//                            Application.ActiveApplication.UserInfoManage.FunctionSuccess(FunctionID.CloseDatasource);
//                        }
//                        else
//                        {
//                            Application.ActiveApplication.UserInfoManage.FunctionFailed(FunctionID.CloseDatasource);
//                        }
//                    }
//                    catch (Exception ex)
//                    {
//                        Application.ActiveApplication.Output.Output(ex, InfoType.Exception, FunctionID.CloseDatasource);
//                    }
//                }
//            }
//        }
//        catch
//        {
//
//        }
//    }
//
//    //将指定数据源添加到最近文件列表中
//    public static void AddDatasourceToRecentFile(Datasource datasource, FunctionID funID)
//    {
//        try
//        {
//            if (datasource != null)
//            {
//                String path = datasource.ConnectionInfo.Server.Replace(Path.AltDirectorySeparatorChar, Path.DirectorySeparatorChar);
//                IRecentFileGroup fileGroup = Application.ActiveApplication.MainForm.RecentFileManager[Properties.CoreResources.String_RecentDatasource];
//                if (fileGroup != null)
//                {
//                    if (fileGroup.Count > 0)
//                    {
//                        fileGroup.Insert(0, datasource.ConnectionInfo.Server);
//                    }
//                    else
//                    {
//                        fileGroup.Add(datasource.ConnectionInfo.Server);
//                    }
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex, InfoType.Exception, funID);
//        }
//    }
//
//    public static String GetAvailableDatasourceAlias(String strAlias, int nIndex)
//    {
//        String strAvailableName = strAlias;
//        try
//        {
//            Datasource datasource = null;
//            if (nIndex == 0)
//            {
//                datasource = Application.ActiveApplication.Workspace.Datasources[strAvailableName];
//            }
//            else
//            {
//                strAvailableName += "_" + nIndex.ToString();
//                datasource = Application.ActiveApplication.Workspace.Datasources[strAvailableName];
//            }
//
//            if (datasource != null)
//            {
//                nIndex++;
//
//                strAvailableName = GetAvailableDatasourceAlias(strAlias, nIndex);
//            }
//        }
//        catch
//        {
//
//        }
//        return strAvailableName;
//    }
//
//    //从一个屏幕坐标点找到对应的控件类型名称或者对应的CtrlAction
//    public static String FindControl(Point point)
//    {
//        String result = String.Empty;
//
//        try
//        {
//            FormBase form = Application.ActiveApplication.MainForm as FormBase;
//            if (form != null)
//            {
//                Point pointForm = form.PointToClient(point);
//                Control control = form.GetChildAtPoint(pointForm);
//                if (control is Ribbon)
//                {
//                    Ribbon ribbon = control as Ribbon;
//                    Point pointRibbon = ribbon.PointToClient(point);
//                    CommandBase command = ribbon.GetCommandAt(pointRibbon);
//                    if ((command is IBaseItem) && (command as IBaseItem).CtrlAction != null)
//                    {
//                        result = (command as IBaseItem).CtrlAction.GetType().FullName;
//                    }
//                }
//                else if (control is _DockBar)
//                {
//                    result = (control as IDockBar).Control.GetType().FullName;
//                }
//                else if (control is UIPanelGroup)
//                {
//                    _DockBar bar = null;
//                    UIPanelGroup group = control as UIPanelGroup;
//
//                    do
//                    {
//                        Point pointGroup = group.PointToClient(point);
//                        Control temp = group.GetChildAtPoint(pointGroup);
//                        if (temp == null)
//                        {
//                            break;
//                        }
//                        else if (temp is _DockBar)
//                        {
//                            bar = temp as _DockBar;
//                        }
//                        else if (temp is UIPanelGroup)
//                        {
//                            group = temp as UIPanelGroup;
//                        }
//                    } while (bar == null);
//
//                    if (bar != null)
//                    {
//                        result = bar.Control.GetType().FullName;
//                    }
//                }
//                else if (control != null)
//                {
//                    result = control.GetType().FullName;
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//
//        return result;
//    }
//
//    public static HelpInfo GetControlURL(Point point)
//    {
//        HelpInfo result = new HelpInfo();
//        try
//        {
//            FormBase form = Application.ActiveApplication.MainForm as FormBase;
//            if (form != null)
//            {
//                Point pointForm = form.PointToClient(point);
//                Control control = form.GetChildAtPoint(pointForm);
//                //这个版本的界面库计算的GetChildAtPoint有问题
//                //if (form.Ribbon.Bounds.Contains(pointForm))
//                //{
//                Ribbon ribbon = form.Ribbon;
//                Point pointRibbon = ribbon.PointToClient(point);
//                CommandBase command = ribbon.GetCommandAt(pointRibbon);
//                if (command != null)
//                {
//                    if ((command is IBaseItem) && (command as IBaseItem).CtrlAction != null)
//                    {
//                        result.IndexPath = (command as IBaseItem).CtrlAction.HelpURL;
//                        CtrlAction ctrlAction = (command as IBaseItem).CtrlAction as CtrlAction;
//                        PluginInfo pluginInfo = ctrlAction.PluginInfo;
//                        if (pluginInfo != null)
//                        {
//                            // 以前写死在\help\WebHelp\ 下了，如果插件没指定位置，则默认为\help\WebHelp\下。
//                            if (pluginInfo.HelpLocalRoot == null || pluginInfo.HelpLocalRoot.Length == 0)
//                            {
//                                result.LocalRoot = pluginInfo.HelpLocalRoot;
//                            }
//                            else
//                            {
//                                result.LocalRoot = "..\\Help\\WebHelp\\";
//                            }
//                            if (pluginInfo.HelpOnlineRoot == null || pluginInfo.HelpOnlineRoot.Length == 0)
//                            {
//                                result.OnlineRoot = pluginInfo.HelpOnlineRoot;
//                            }
//                            else
//                            {
//                                result.OnlineRoot = _XMLTag.OnlineHelpAddress;
//                            }
//                        }
//                        else
//                        {
//                            result.LocalRoot = "..\\Help\\WebHelp\\";
//                            result.OnlineRoot = _XMLTag.OnlineHelpAddress;
//                        }
//
//                    }
//                }
//                else if (control is UIPanelGroup)
//                {
//                    _DockBar bar = null;
//                    UIPanelGroup group = control as UIPanelGroup;
//
//                    do
//                    {
//                        Point pointGroup = group.PointToClient(point);
//                        Control temp = group.GetChildAtPoint(pointGroup);
//                        if (temp == null)
//                        {
//                            break;
//                        }
//                        else if (temp is _DockBar)
//                        {
//                            bar = temp as _DockBar;
//                        }
//                        else if (temp is UIPanelGroup)
//                        {
//                            group = temp as UIPanelGroup;
//                        }
//                    } while (bar == null);
//
//                    if (bar != null)
//                    {
//                        result.IndexPath = (bar as IHelp).HelpURL;
//                        PluginInfo pluginInfo = bar.PluginInfo;
//                        if (pluginInfo != null)
//                        {
//                            result.LocalRoot = pluginInfo.HelpLocalRoot;
//                            result.OnlineRoot = pluginInfo.HelpOnlineRoot;
//                        }
//                        else
//                        {
//                            result.LocalRoot = "..\\Help\\WebHelp\\";
//                            result.OnlineRoot = _XMLTag.g_AttributionHelpOnlineAddress;
//                        }
//                    }
//                }
//                else if (control is IHelp)
//                {
//                    result.IndexPath = (control as IHelp).HelpURL;
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//        return result;
//    }
//
//    public static boolean IsNumericField(SuperMap.Data.FieldInfo fieldInfo)
//    {
//        boolean isNumericField = false;
//        try
//        {
//            if (fieldInfo.Type == FieldType.Double ||
//                fieldInfo.Type == FieldType.Single ||
//                fieldInfo.Type == FieldType.Int16 ||
//                fieldInfo.Type == FieldType.int ||
//                fieldInfo.Type == FieldType.Int64 ||
//                fieldInfo.Type == FieldType.Byte)
//            {
//                isNumericField = true;
//            }
//        }
//        catch
//        {
//            // 旧格式的缓存文件，fieldInfo.Type 会导致异常，旧数据暂时不会处理，这里不处理此异常
//        }
//        return isNumericField;
//    }
//
//    public static int TypeIndexOfDataset(object dataset)
//    {
//        int indexResult = -1;
//        try
//        {
//            if (dataset is Dataset)
//            {
//                if (dataset is DatasetVector)
//                {
//                    indexResult = 0;
//                }
//                else if (dataset is DatasetImage)
//                {
//                    indexResult = 1;
//                }
//                else if (dataset is DatasetGrid)
//                {
//                    indexResult = 2;
//                }
//                else if (dataset is DatasetImageCollection)
//                {
//                    indexResult = 3;
//                }
//                else if ((dataset as Dataset).Type == DatasetType.ParametricRegion
//                    || (dataset as Dataset).Type == DatasetType.ParametricLine)
//                {
//                    indexResult = 4;
//                }
//            }
//            else if (dataset is DatasetGroup)
//            {
//                indexResult = 5;
//            }
//        }
//        catch (System.Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//        return indexResult;
//    }
//
//    public static int IndexOfDataset(Dataset dataset)
//    {
//        int indexResult = -1;
//        try
//        {
//            int index = 0;
//            Datasets datasets = dataset.Datasource.Datasets;
//            foreach (Dataset tempDT in datasets)
//            {
//                if (tempDT == dataset)
//                {
//                    indexResult = index;
//                    break;
//                }
//
//                index++;
//            }
//        }
//        catch
//        {
//
//        }
//
//        return indexResult;
//    }
//
//    public static int IndexOfDatasetGroup(DatasetGroup datasetGroup)
//    {
//        int indexResult = -1;
//        try
//        {
//            int index = 0;
//            DatasetGroup group = datasetGroup.Datasource.RootGroup;
//            foreach (DatasetGroup tempGroup in group.ChildGroups)
//            {
//                if (tempGroup == datasetGroup)
//                {
//                    indexResult = index;
//                    break;
//                }
//                index++;
//            }
//        }
//        catch
//        {
//
//        }
//        return indexResult;
//    }
//
//    public static boolean IsMarkerGeometry(Geometry geometry)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geometry == null)
//            {
//            }
//            else if (geometry.Type == GeometryType.GeoPoint)
//            //|| geometry.Type == GeometryType.GeoPoint3D)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsCompoundContainMarker(GeoCompound geoCompound)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geoCompound == null)
//            {
//            }
//            else
//            {
//                for (int i = 0; i < geoCompound.PartCount; i++)
//                {
//                    if (IsMarkerGeometry(geoCompound[i]))
//                    {
//                        bResult = true;
//                        break;
//                    }
//                }
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsLineGeometry(Geometry geometry)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geometry == null)
//            {
//            }
//            else if (geometry.Type == GeometryType.GeoLine ||
//                geometry.Type == GeometryType.GeoArc ||
//                geometry.Type == GeometryType.GeoEllipticArc ||
//                geometry.Type == GeometryType.GeoCurve ||
//                geometry.Type == GeometryType.GeoBSpline ||
//                //geometry.Type == GeometryType.GeoLineM ||
//                geometry.Type == GeometryType.GeoCardinal ||
//                //geometry.Type == GeometryType.GeoLine3D ||
//                geometry.Type == GeometryType.GeoChord)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsCompoundContainLine(GeoCompound geoCompound)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geoCompound == null)
//            {
//            }
//            else
//            {
//                for (int i = 0; i < geoCompound.PartCount; i++)
//                {
//                    if (IsLineGeometry(geoCompound[i]))
//                    {
//                        bResult = true;
//                        break;
//                    }
//                }
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//    //GeoChord = 23,
//    //GeoLineM = 35,
//    //GeoPoint3D = 101,
//    //GeoLine3D = 103,
//    //GeoRegion3D = 105,
//    //GeoPlacemark = 108,
//    //GeoCompound = 1000,
//    //GeoPicture = 1101,
//    //GeoModel = 1201,
//    //GeoPicture3D = 1202,
//    //GeoSphere = 1203,
//    //GeoHemiSphere = 1204,
//    //GeoBox = 1205,
//    //GeoCylinder = 1206,
//    //GeoCone = 1207,
//    //GeoPyramid = 1208,
//    //GeoPie3D = 1209,
//    //GeoCircle3D = 1210,
//    //GeoPieCylinder = 1211,
//    //GeoEllipsoid = 1212,
//    //GeoMapScale = 2005,
//    //GeoNorthArrow = 2008,
//    //GeoMapBorder = 2009,
//    public static boolean IsRegionGeometry(Geometry geometry)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geometry == null)
//            {
//            }
//            else if (geometry.Type == GeometryType.GeoRegion ||
//                //geometry.Type == GeometryType.GeoRegion3D ||
//                geometry.Type == GeometryType.GeoRectangle ||
//                geometry.Type == GeometryType.GeoRoundRectangle ||
//                geometry.Type == GeometryType.GeoCircle ||
//                geometry.Type == GeometryType.GeoEllipse ||
//                geometry.Type == GeometryType.GeoPie)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsCompoundContainRegion(GeoCompound geoCompound)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geoCompound == null)
//            {
//            }
//            else
//            {
//                for (int i = 0; i < geoCompound.PartCount; i++)
//                {
//                    if (IsRegionGeometry(geoCompound[i]))
//                    {
//                        bResult = true;
//                        break;
//                    }
//                }
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsMapGeometry(Geometry geometry)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geometry == null)
//            {
//            }
//            else if (geometry.Type == GeometryType.GeoMap)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsTextGeometry(Geometry geometry)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geometry == null)
//            {
//            }
//            else if (geometry.Type == GeometryType.GeoText ||
//                geometry.Type == GeometryType.GeoText3D)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsCompoundContainText(GeoCompound geoCompound)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (geoCompound == null)
//            {
//            }
//            else
//            {
//                for (int i = 0; i < geoCompound.PartCount; i++)
//                {
//                    if (IsTextGeometry(geoCompound[i]))
//                    {
//                        bResult = true;
//                        break;
//                    }
//                }
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static String GetSuitableSymbolStyleName(GeometryType geometryType)
//    {
//        String name = String.Empty;
//        try
//        {
//            switch (geometryType)
//            {
//                case GeometryType.GeoLine:
//                    {
//                        name = Properties.CoreResources.String_LineStyle;
//                    }
//                    break;
//                case GeometryType.GeoPoint:
//                    {
//                        name = Properties.CoreResources.String_SymbolStyle;
//                    }
//                    break;
//                case GeometryType.GeoRegion:
//                    {
//                        name = Properties.CoreResources.String_FillStyle;
//                    }
//                    break;
//                case GeometryType.GeoText:
//                    {
//                        name = Properties.CoreResources.String_TextStyle;
//                    }
//                    break;
//
//                default:
//                    {
//                    }
//                    break;
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//        return name;
//    }
//
//    //一个屏幕像素在地图上的长度
//    public static double PixelLength(MapControl mapControl)
//    {
//        Point2D pnt1 = mapControl.Map.PixelToMap(new Point(1, 0));
//        Point2D pnt2 = mapControl.Map.PixelToMap(new Point(0, 0));
//        double x = Math.Abs(pnt1.X - pnt2.X);
//        double y = Math.Abs(pnt1.Y - pnt2.Y);
//        return Math.Sqrt(x * x + y * y);
//    }
//
//    //public static double GetMMPerPixel()
//    //{
//    //    double result = 1.0;
//    //    try
//    //    {
//    //        Font font1 = new Font(FontFamily.GenericMonospace, 12.0F, FontStyle.Regular, GraphicsUnit.Pixel);
//    //        double pixel = Convert.ToDouble(font1.Height);
//    //        Font font2 = new Font(FontFamily.GenericMonospace, 12.0F, FontStyle.Regular, GraphicsUnit.Millimeter);
//    //        double mm = Convert.ToDouble(font2.Height);
//    //        result = pixel / mm;
//    //    }
//    //    catch (Exception ex)
//    //    {
//    //        Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//    //    }
//    //    return result;
//    //}             
//
//    //public static boolean IsSuperMapAssembly(String strFullName)
//    //{
//    //    boolean bIsSuperMapAssembly = false;
//    //    try
//    //    {
//    //        String[] s = strFullName.Split(',');
//    //        for (int i = 0; i < s.Length; i++)
//    //        {
//    //            String str = s[i].TrimStart();
//    //            if (str.StartsWith("PublicKeyToken"))
//    //            {
//    //                String strPublicKeyToken = str.Replace("PublicKeyToken=", "");
//    //                if (strPublicKeyToken.Equals(g_strDesktopPublicKeyToken) ||
//    //                    strPublicKeyToken.Equals(g_strTestPublicKeyToken))
//    //                {
//    //                    bIsSuperMapAssembly = true;
//    //                }
//    //                break;
//    //            }
//    //        }
//    //    }
//    //    catch (System.Exception ex)
//    //    {
//    //        Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//    //    }
//    //    return bIsSuperMapAssembly;
//    //}
//
//    //public static boolean IsSuperMapAssembly(PluginInfo info)
//    //{
//    //    boolean bIsSuperMapAssembly = false;
//    //    try
//    //    {
//    //        String path = CommonToolkit.PathWrap.GetFullPathName(info.AssemblyName);
//    //        if (System.IO.File.Exists(path))
//    //        {
//    //            System.Reflection.Assembly assembly = System.Reflection.Assembly.LoadFrom(path);
//    //            bIsSuperMapAssembly = IsSuperMapAssembly(assembly.FullName);
//    //        }
//    //    }
//    //    catch (System.Exception ex)
//    //    {
//    //        Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//    //    }
//    //    return bIsSuperMapAssembly;
//    //}
//
//    public static boolean IsDatasetShowed(Dataset dataset)
//    {
//        boolean result = false;
//        try
//        {
//            IFormManager formManager = Application.ActiveApplication.MainForm.FormManager;
//            for (int i = 0; i < formManager.Count; i++)
//            {
//                IForm form = formManager[i];
//                if (form is IFormMap)
//                {
//                    IFormMap formMap = form as IFormMap;
//                    List<Layer> layers = CommonToolkit.MapWrap.GetLayers(formMap.MapControl.Map);
//                    foreach (Layer layer in layers)
//                    {
//                        if (layer.Dataset == dataset)
//                        {
//                            result = true;
//                            break;
//                        }
//                    }
//                }
//                else if (form is IFormScene)
//                {
//                    IFormScene formScene = form as IFormScene;
//                    for (int j = 0; j < formScene.SceneControl.Scene.TerrainLayers.Count; j++)
//                    {
//                        String dataName = formScene.SceneControl.Scene.TerrainLayers[j].DataName;
//                        if (dataName.Contains("@"))
//                        {
//                            int index = dataName.IndexOf('@');
//                            String datasetName = dataName.Substring(0, index);
//                            String datasourceName = dataName.Substring(index + 1);
//                            if (dataName == dataset.Name &&
//                                datasourceName == dataset.Datasource.Alias)
//                            {
//                                result = true;
//                                break;
//                            }
//                        }
//                    }
//                    for (int j = 0; j < formScene.SceneControl.Scene.TerrainLayers.Count; j++)
//                    {
//                        if ((formScene.SceneControl.Scene.Layers[j] is Layer3DDataset) &&
//                            (formScene.SceneControl.Scene.Layers[j] as Layer3DDataset).Dataset == dataset)
//                        {
//                            result = true;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.Message, InfoType.Exception);
//        }
//        return result;
//    }
//
//    public static void CommonPrjCoordSys(IBaseItem dropdownButton, String GalleryID, DataType type)
//    {
//        try
//        {
//            _ButtonDropdown buttonDrop = dropdownButton as _ButtonDropdown;
//            _Gallery gallery = buttonDrop.SubItems[GalleryID] as _Gallery;
//            gallery.ItemsWidth = 200;
//            gallery.MaxGalleryColumns = 1;
//            gallery.ItemsImageAlignment = ContentAlignment.MiddleRight;
//            gallery.ItemsTextAlignment = ContentAlignment.MiddleLeft;
//            if (gallery != null)
//            {
//                gallery.GalleryItems.Clear();
//
//                // 新建Title
//                _ButtonGallery item = new _ButtonGallery();
//                item.Text = Properties.CoreResources.String_CommonPrjCoordSys;
//                item.ItemType = GalleryItemType.GalleryTitle;
//                item.Enabled = false;
//                gallery.GalleryItems.Insert(0, item);
//
//                // 初始化常用投影
//                TreeNode treeNode = _LoadMyFavoritePrjCoordSys.GetMyFavoritePrjCoordSysListFromDisk();
//                InitializeFavoriteMenuItems(treeNode, gallery, type);
//
//                gallery.MaxGalleryRows = gallery.GalleryItems.Count;
//            }
//        }
//        catch
//        {
//        }
//    }
//
//    public static void InitializeFavoriteMenuItems(TreeNode parentNode, _Gallery gallery, DataType type)
//    {
//        try
//        {
//            if (parentNode.Nodes.Count > 0)
//            {
//                foreach (TreeNode treeNode in parentNode.Nodes)
//                {
//                    InitializeFavoriteMenuItems(treeNode, gallery, type);
//                }
//            }
//            else
//            {
//                if (Path.GetExtension(parentNode.Tag.ToString()).Length > 0)
//                {
//                    _ButtonGallery item = new _ButtonGallery();
//                    item.Text = Path.GetFileNameWithoutExtension(parentNode.Tag.ToString());
//                    item.Tag = type;
//                    item.SuperTipSettings.Text = parentNode.Tag.ToString();
//                    _CtrlActionCommonPrjCoordSys commonPrj = new _CtrlActionCommonPrjCoordSys();
//                    commonPrj.Caller = item;
//                    item.CtrlAction = commonPrj;
//                    gallery.GalleryItems.Insert(gallery.GalleryItems.Count, item);
//                }
//            }
//        }
//        catch
//        {
//
//        }
//    }
//
//    public static TextStyle GetCurrentTextStyle(IButtonGallery ButtonGallery)
//    {
//        TextStyle textStyle = null;
//        try
//        {
//            String strName = CommonToolkit.PathWrap.GetFullPathName(ButtonGallery.CustomProperty);
//
//            if (File.Exists(strName))
//            {
//                textStyle = new TextStyle();
//
//                textStyle.FromXML(File.ReadAllText(strName, Encoding.UTF8));
//            }
//        }
//        catch
//        {
//
//        }
//        return textStyle;
//    }
//
//    //转换状态栏为label
//    public static void SetLabelStatusBarText(IBaseItem caller, String strLabel)
//    {
//        try
//        {
//            _LabelStatusBar label = caller as _LabelStatusBar;
//            if (label != null)
//            {
//                label.Text = strLabel;
//            }
//        }
//        catch
//        {
//
//        }
//    }
//
//    // 应用多时可写到接口
//    static private boolean m_showSelectedLayerslegend;
//    public static boolean ShowSelectedLayerslegend
//    {
//        get
//        {
//            return m_showSelectedLayerslegend;
//        }
//        set
//        {
//            m_showSelectedLayerslegend = value;
//        }
//    }
//
//    // {{Note by hucp 2011-1-10
//    // 获取点到线的最短距离以及对应的垂足（端点）坐标
//    // }}Note by hucp 2011-1-10
//    public static Double GetMinDistance(Point2D breakPoint, Point2Ds desLinePoints, Double tolerance, ref Point2D perpendicularFoot, ref int nSegment)
//    {
//        Double dDistance = 0;
//        try
//        {
//            if (desLinePoints != null)
//            {
//                int nDesPointCount = desLinePoints.Count;
//                Double dTempDistance = 0;
//                Point2D pntTemp = Point2D.Empty;
//                for (int i = 0; i < nDesPointCount - 1; i++)
//                {
//                    // 会出现起始点和终点重合的情况，这里做一下处理
//                    try
//                    {
//                        pntTemp = Geometrist.ComputePerpendicularPosition(breakPoint, desLinePoints[i], desLinePoints[i + 1]);
//
//                        // 如果垂足在线段上，取点到垂足的距离
//                        if (pntTemp != Point2D.Empty
//                            && IsPointInLineRect(pntTemp, desLinePoints[i], desLinePoints[i + 1], tolerance))
//                        //if (Geometrist.IsPointOnLine(perpendicularFoot, desLinePoints[i], desLinePoints[i + 1], false))
//                        {
//                            Double dOffsetX = pntTemp.X - breakPoint.X;
//                            Double dOffsetY = pntTemp.Y - breakPoint.Y;
//                            dTempDistance = Math.Sqrt(dOffsetX * dOffsetX + dOffsetY * dOffsetY);
//                            if (i == 0)
//                            {
//                                dDistance = dTempDistance;
//                                perpendicularFoot = pntTemp;
//                                nSegment = i;
//                            }
//                        }
//                        // 如果垂足在延长线上，则取点到线的两个端点的最小距离，否则取点到垂足的距离
//                        else
//                        {
//                            Double dOffsetX = desLinePoints[i].X - breakPoint.X;
//                            Double dOffsetY = desLinePoints[i].Y - breakPoint.Y;
//                            dTempDistance = Math.Sqrt(dOffsetX * dOffsetX + dOffsetY * dOffsetY);
//                            pntTemp = desLinePoints[i];
//                            if (i == 0)
//                            {
//                                dDistance = dTempDistance;
//                                nSegment = i;
//                            }
//
//                            if (i == nDesPointCount - 2) // 最后一段
//                            {
//                                dOffsetX = desLinePoints[i + 1].X - breakPoint.X;
//                                dOffsetY = desLinePoints[i + 1].Y - breakPoint.Y;
//                                Double dTempDistance2 = Math.Sqrt(dOffsetX * dOffsetX + dOffsetY * dOffsetY);
//                                if (dTempDistance > dTempDistance2)
//                                {
//                                    dTempDistance = dTempDistance2;
//                                    pntTemp = desLinePoints[i + 1];
//                                    i++;
//                                }
//                            }
//                        }
//
//                        if (dDistance > dTempDistance) // 找到更小的距离后再次赋值
//                        {
//                            dDistance = dTempDistance;
//                            perpendicularFoot = pntTemp;
//                            nSegment = i;
//                        }
//                    }
//                    catch
//                    {
//                    }
//                }
//            }
//        }
//        catch (System.Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//        return dDistance;
//    }
//
//    // {{Note by hucp 2011-1-20
//    // 判断点是否在线段的外接矩形内
//    // }}Note by hucp 2011-1-20
//    public static boolean IsPointInLineRect(Point2D point, Point2D pntStart, Point2D pntEnd, Double tolerance)
//    {
//        boolean bResult = false;
//        try
//        {
//            if (point != Point2D.Empty
//                && point.X >= Math.Min(pntStart.X, pntEnd.X) - tolerance
//                && point.X <= Math.Max(pntStart.X, pntEnd.X) + tolerance
//                && point.Y >= Math.Min(pntStart.Y, pntEnd.Y) - tolerance
//                && point.Y <= Math.Max(pntStart.Y, pntEnd.Y) + tolerance)
//            {
//                bResult = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bResult;
//    }
//
//    public static boolean IsPntLeft(Point2D pntFrom, Point2D pntTo, Point2D pntTest)
//    {
//        boolean bLeft = false;
//        try
//        {
//            if ((pntTest.Y - pntTo.Y) * (pntTo.X - pntFrom.X) > (pntTo.Y - pntFrom.Y) * (pntTest.X - pntTo.X))
//            {
//                bLeft = true;
//            }
//        }
//        catch
//        {
//
//        }
//        return bLeft;
//    }
//
//    //格式化时间间隔为字符串形式表示
//    public static String FormatTimeSpan(TimeSpan timeSpan)
//    {
//        String result = String.Empty;
//        try
//        {
//            if (timeSpan.Days > 0)
//            {
//                result = result + timeSpan.Days.ToString() +
//                    Properties.CoreResources.String_Time_Days;
//            }
//            if (timeSpan.Hours > 0)
//            {
//                result = result + timeSpan.Hours.ToString() +
//                    Properties.CoreResources.String_Time_Hours;
//            }
//            if (timeSpan.Minutes > 0)
//            {
//                result = result + timeSpan.Minutes.ToString() +
//                    Properties.CoreResources.String_Time_Minutes;
//            }
//            if (timeSpan.Seconds > 0)
//            {
//                if (timeSpan.TotalSeconds >= 60) //过1分种时不输出小数
//                {
//                    result = result + timeSpan.Seconds.ToString() +
//                        Properties.CoreResources.String_Time_Seconds;
//                }
//                else // 小于1分钟时输出小数
//                {
//                    result = timeSpan.TotalSeconds.ToString("F02") +
//                        Properties.CoreResources.String_Time_Seconds;
//                }
//            }
//
//            if (timeSpan.TotalSeconds < 1)
//            {
//                result = timeSpan.TotalSeconds.ToString("F02") +
//                    Properties.CoreResources.String_Time_Seconds;
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//        return result;
//    }
//    /// <summary>
//    /// 检查文件或文件夹路径格式是否合法，可以不存在
//    /// </summary>
//    /// <param name="path">待检查的路径</param>
//    /// <returns></returns>
//    public static bool IsPathFormatCorrect(String path)
//    {
//        boolean result = false;
//        try
//        {
//            if (path != String.Empty)
//            {
//                String rule = "^[a-zA-Z]:(((\\\\(?! )[^\\\\/:*?\"<>|]+)+\\\\?)|(\\\\))\\s*$";
//                Regex regex = new Regex(rule);
//                result = regex.IsMatch(path);
//            }
//        }
//        catch
//        {
//        }
//        return result;
//    }
//
//    public static boolean IsBrowseAction(SuperMap.UI.Action action)
//    {
//        boolean result = false;
//        if (action == SuperMap.UI.Action.ZoomOut ||
//               action == SuperMap.UI.Action.ZoomIn ||
//               action == SuperMap.UI.Action.ZoomFree2 ||
//               action == SuperMap.UI.Action.ZoomFree ||
//               action == SuperMap.UI.Action.Pan2 ||
//               action == SuperMap.UI.Action.Pan)
//        {
//            result = true;
//        }
//        return result;
//    }
//
//    /// <summary>
//    /// 清空图层管理器中的数据
//    /// </summary>
//    /// <returns></returns>
//    public static void ResetLayersControlManagerData()
//    {
//        IDockBar dockbar = Application.ActiveApplication.MainForm.DockBarManager[typeof(LayersControlManager)];
//        if (dockbar != null)
//        {
//            LayersControlManager layersControlManager = dockbar.Control as LayersControlManager;
//            layersControlManager.Map = null;
//            layersControlManager.Scene = null;
//        }
//    }
//
//    static private List<Layer3DsTreeNodeBase> m_listCopyLayer3DsTreeNode;
//    public static Layer3DsTreeNodeBase[] CopyLayer3DsTreeNodes
//    {
//        get
//        {
//            return m_listCopyLayer3DsTreeNode.ToArray();
//        }
//        set
//        {
//            m_listCopyLayer3DsTreeNode = new List<Layer3DsTreeNodeBase>();
//            m_listCopyLayer3DsTreeNode.AddRange(value);
//        }
//    }
//
//    ////  [7/21/2011 zhoujt]
//    //public static int GetChartSelectionCount(Layer layer)
//    //{
//    //    int nSelection = 0;
//    //    LayerChart layerChart = layer as LayerChart;
//    //    if (layerChart != null)
//    //    {
//    //        foreach (Selection selection in layerChart.Selections)
//    //        {
//    //            nSelection += selection.Count;
//    //        }
//    //    }
//
//    //    return nSelection;
//    //}
//
//    //public static int GetChartRecordsetCount(Layer layer)
//    //{
//    //    int nRecordCount = 0;
//    //    LayerChart layerChart = layer as LayerChart;
//    //    if (layerChart != null)
//    //    {
//    //        foreach (Selection selection in layerChart.Selections)
//    //        {
//    //            nRecordCount += selection.Dataset.RecordCount;
//    //        }
//    //    }
//
//    //    return nRecordCount;
//    //}
//
//    ////// 增加打开子窗口之后的委托，用于注册相关的事件 [8/12/2011 zhoujt]
//    ////public static event FormChildOpenedEventHandler FormChildOpenedEvent;
//    ////public delegate void FormChildOpenedEventHandler(Object sender, WindowEventArgs e);
//    ////public class WindowEventArgs : EventArgs
//    ////{
//    ////    public WindowEventArgs(IForm form)
//    ////    {
//    ////        m_form = form;
//    ////    }
//
//    ////    private IForm m_form;
//    ////    public IForm CurrentForm
//    ////    {
//    ////        get
//    ////        {
//    ////            return m_form;
//    ////        }
//    ////    }
//    ////}
//
//    ////public static void SendFormChildOpenedEvent(IForm form)
//    ////{
//    ////    try
//    ////    {
//    ////        if (FormChildOpenedEvent != null)
//    ////        {
//    ////            FormChildOpenedEvent(null, new WindowEventArgs(form));
//    ////        }
//    ////    }
//    ////    catch
//    ////    {
//
//    ////    }
//    ////}
//
//
//    ////// 增加关闭子窗口的委托，用于反注册相关事件 [8/12/2011 zhoujt]
//    ////public static event FormChildClosedEventHandler FormChildClosedEvent;
//    ////public delegate void FormChildClosedEventHandler(Object sender, WindowEventArgs e);
//    ////public static void SendFormChildClosedEvent(IForm form)
//    ////{
//    ////    try
//    ////    {
//    ////        if (FormChildClosedEvent != null)
//    ////        {
//    ////            FormChildClosedEvent(null, new WindowEventArgs(form));
//    ////        }
//    ////    }
//    ////    catch
//    ////    {
//
//    ////    }
//    ////}               
//
//    /// <summary>
//    /// 获取文件流的字符编码
//    /// </summary>
//    /// <param name="stream"></param>
//    /// <returns></returns>
//    public static Encoding GetEncoding(FileStream stream)
//    {
//        Encoding resultEncoding = null;
//        try
//        {
//            if (stream != null && stream.Length >= 2)
//            {
//                //保存文件流的前4个字节
//                byte byte1 = 0;
//                byte byte2 = 0;
//                byte byte3 = 0;
//                byte byte4 = 0;
//                //保存当前Seek位置
//                long origPos = stream.Seek(0, SeekOrigin.Begin);
//                stream.Seek(0, SeekOrigin.Begin);
//
//                int nByte = stream.ReadByte();
//                byte1 = Convert.ToByte(nByte);
//                byte2 = Convert.ToByte(stream.ReadByte());
//                if (stream.Length >= 3)
//                {
//                    byte3 = Convert.ToByte(stream.ReadByte());
//                }
//                if (stream.Length >= 4)
//                {
//                    byte4 = Convert.ToByte(stream.ReadByte());
//                }
//                //根据文件流的前4个字节判断Encoding
//                //Unicode {0xFF, 0xFE};
//                //BE-Unicode {0xFE, 0xFF};
//                //UTF8 = {0xEF, 0xBB, 0xBF};
//                if (byte1 == 0xFE && byte2 == 0xFF)//UnicodeBe
//                {
//                    resultEncoding = Encoding.BigEndianUnicode;
//                }
//                if (byte1 == 0xFF && byte2 == 0xFE && byte3 != 0xFF)//Unicode
//                {
//                    resultEncoding = Encoding.Unicode;
//                }
//                if (byte1 == 0xEF && byte2 == 0xBB && byte3 == 0xBF)//UTF8
//                {
//                    resultEncoding = Encoding.UTF8;
//                }
//                //恢复Seek位置       
//                stream.Seek(origPos, SeekOrigin.Begin);
//            }
//        }
//        catch
//        {
//        }
//        return resultEncoding;
//    }
//
//    /// <summary>
//    /// 获取节点，忽略大小写,只处理NODE/node/Node三种情况
//    /// </summary>
//    public static XmlNode GetChildNodeIgnoreCase(XmlNode parentNode, String childName)
//    {
//        XmlNode result = null;
//        try
//        {
//            result = parentNode[childName];//原
//            if (result == null)
//            {
//                childName = childName.ToUpper();//NODE
//                result = parentNode[childName];
//            }
//            if (result == null)
//            {
//                childName = childName.ToLower();//node
//                result = parentNode[childName];
//            }
//            if (result == null)
//            {
//                childName = childName[0].ToString().ToUpper() +
//                    childName.Remove(0, 1);//Node
//                result = parentNode[childName];
//            }
//        }
//        catch
//        {
//        }
//        return result;
//    }
//
//    //{{ 适用于对精度要求不高的判断 [7/17/2012 huchenpu]
//    public static boolean IsZeroLowPrecision(Double value)
//    {
//        boolean isZero = false;
//        try
//        {
//            if (Math.Abs(value) < 0.0000000001)
//            {
//                isZero = true;
//            }
//        }
//        catch
//        {
//        }
//        return isZero;
//    }
//    //}}  [7/17/2012 huchenpu]
//
//    //{{ 适用于地图缓存，分级配图等高精度的判断 [7/17/2012 huchenpu]
//    public static boolean IsZero(Double value)
//    {
//        boolean isZero = false;
//        try
//        {
//            if (Math.Abs(value) <= Double.Epsilon)
//            //if (Math.Abs(value) < 0.0000000000001)
//            {
//                isZero = true;
//            }
//        }
//        catch
//        {
//        }
//        return isZero;
//    }
//    //}}  [7/17/2012 huchenpu]
//
//    // 获取数据集的默认容限
//    public static Tolerance GetDefaultTolerance(DatasetVector dataset)
//    {
//        Tolerance tolerance = null;
//        try
//        {
//            if (dataset != null)
//            {
//                tolerance = new Tolerance();
//                Double extent = Math.Max(dataset.Bounds.Height, dataset.Bounds.Width);
//
//                tolerance.NodeSnap = extent / 1000000.0f;
//                tolerance.Dangle = extent / 10000.0f;
//                tolerance.Extend = extent / 10000.0f;
//                tolerance.SmallPolygon = 0.0;
//                tolerance.Grain = extent / 1000.0f;
//                if (dataset.Type == DatasetType.Region)
//                {
//                    boolean isOpen = dataset.IsOpen;
//                    int fieldIndex = dataset.FieldInfos.IndexOf("SMAREA");
//                    if (fieldIndex >= 0)
//                    {
//                        Double maxArea = dataset.Statistic(fieldIndex, StatisticMode.Max);
//                        tolerance.SmallPolygon = maxArea / 1000000.0f;
//                    }
//                    if (!isOpen)
//                    {
//                        dataset.Close();
//                    }
//                }
//            }
//        }
//        catch
//        {
//        }
//        return tolerance;
//    }
//
//    /// <summary>
//    /// 数据集排序
//    /// </summary>
//    /// <param name="datasets"></param>
//    /// <param name="field">排序字段</param>
//    /// <param name="mode">排序方法</param>
//    public static void DatasetSort(ref Dataset[] datasets, DatasetSortField field, SortOrder mode)
//    {
//        try
//        {
//            int j;
//
//            for (int gap = datasets.Length / 2; gap > 0; gap /= 2)
//            {
//                for (int i = gap; i < datasets.Length; i++)
//                {
//                    Dataset tempDataset = datasets[i];
//
//                    String tempFieldStringLeft = field == DatasetSortField.Name ? tempDataset.Name : tempDataset.Type.ToString();
//
//                    if (mode == SortOrder.Ascending)
//                    {
//                        for (j = i; j >= gap && tempFieldStringLeft.CompareTo(
//                            field == DatasetSortField.Name ? datasets[j - gap].Name : datasets[j - gap].Type.ToString()) < 0; j -= gap)
//                        {
//                            datasets[j] = datasets[j - gap];
//                        }
//                        datasets[j] = tempDataset;
//                    }
//                    else if (mode == SortOrder.Descending)
//                    {
//                        for (j = i; j >= gap && tempFieldStringLeft.CompareTo(
//                            field == DatasetSortField.Name ? datasets[j - gap].Name : datasets[j - gap].Type.ToString()) > 0; j -= gap)
//                        {
//                            datasets[j] = datasets[j - gap];
//                        }
//                        datasets[j] = tempDataset;
//                    }
//                }
//            }
//        }
//        catch
//        {
//        }
//    }
//
//    public static String GetCaptionByScale(Double scale, boolean integral)
//    {
//        String caption = String.Empty;
//        try
//        {
//            // 用 Decimal 解决精度丢失的问题
//            if (integral)
//            {
//                caption = "1:" + (Convert.Toint(1.0m / (Decimal)scale)).ToString();
//            }
//            else
//            {
//                double temp = 1.0 / scale;
//                //caption = "1:" + (Convert.ToDouble(1.0m / (Decimal)scale)).ToString("r");
//                caption = "1:" + temp.ToString("r");
//            }
//        }
//        catch
//        {
//            caption = String.Empty;
//        }
//
//        return caption;
//    }
//
//    public static Double GetScaleByCaption(String caption)
//    {
//        // 用 Decimal 解决精度丢失的问题
//        Double scale = 0;
//        Double value = 0;
//        try
//        {
//            if (caption.IndexOf("1:") != -1)
//            {
//                caption = caption.Substring(2);
//            }
//            //采用Decimal会Parse失败，采用Double来转化。Note by tangzhq，2012-8-9
//            //Decimal.TryParse(caption, out scale);
//            value = Convert.ToDouble(caption);
//            if (!Toolkit.IsZero(value))
//            {
//                scale = (1.0 / value);
//            }
//        }
//        catch
//        {
//        }
//        return scale;
//        //return Convert.ToDouble(1.0m / scale);
//    }
//
//    public static boolean IsUnicode()
//    {
//        // 组件已提供API，直接调用即可
//        return SuperMap.Data.Environment.IsUnicodeVersion;
//    }
//
//    /// <summary>
//    /// 创建圆角矩形
//    /// </summary>
//    /// <param name="rect">矩形区域</param>
//    /// <param name="cornerRadius">圆角半径</param>
//    /// <returns></returns>
//    public static GraphicsPath CreateRoundedRectanglePath(Rectangle rect, int cornerRadius)
//    {
//        GraphicsPath roundedRect = new GraphicsPath();
//        roundedRect.AddArc(rect.X, rect.Y, cornerRadius * 2, cornerRadius * 2, 180, 90);
//        roundedRect.AddLine(rect.X + cornerRadius, rect.Y, rect.Right - cornerRadius * 2, rect.Y);
//        roundedRect.AddArc(rect.X + rect.Width - cornerRadius * 2, rect.Y, cornerRadius * 2, cornerRadius * 2, 270, 90);
//        roundedRect.AddLine(rect.Right, rect.Y + cornerRadius * 2, rect.Right, rect.Y + rect.Height - cornerRadius * 2);
//        roundedRect.AddArc(rect.X + rect.Width - cornerRadius * 2, rect.Y + rect.Height - cornerRadius * 2, cornerRadius * 2, cornerRadius * 2, 0, 90);
//        roundedRect.AddLine(rect.Right - cornerRadius * 2, rect.Bottom, rect.X + cornerRadius * 2, rect.Bottom);
//        roundedRect.AddArc(rect.X, rect.Bottom - cornerRadius * 2, cornerRadius * 2, cornerRadius * 2, 90, 90);
//        roundedRect.AddLine(rect.X, rect.Bottom - cornerRadius * 2, rect.X, rect.Y + cornerRadius * 2);
//        roundedRect.CloseFigure();
//        return roundedRect;
//    }
//
//    /// <summary>
//    /// 创建带三角形顶的圆角矩形
//    /// </summary>
//    /// <param name="rect">矩形区域</param>
//    /// <param name="cornerRadius">圆角半径</param>
//    /// <param name="startPoint"></param>
//    /// <returns></returns>
//    public static GraphicsPath CreateRoundedRectanglePathEx(Rectangle rect, int cornerRadius, Point startPoint)
//    {
//        GraphicsPath roundedRect = new GraphicsPath();
//        roundedRect.AddArc(rect.X, rect.Y, cornerRadius * 2, cornerRadius * 2, 180, 90);
//        roundedRect.AddLine(rect.X + cornerRadius, rect.Y, startPoint.X - 10, rect.Y);
//        roundedRect.AddLine(startPoint.X - 10, rect.Y, startPoint.X, startPoint.Y);
//        roundedRect.AddLine(startPoint.X, startPoint.Y, startPoint.X + 10, rect.Y);
//        roundedRect.AddLine(startPoint.X + 10, rect.Y, rect.Right - cornerRadius * 2, rect.Y);
//
//        roundedRect.AddArc(rect.X + rect.Width - cornerRadius * 2, rect.Y, cornerRadius * 2, cornerRadius * 2, 270, 90);
//        roundedRect.AddLine(rect.Right, rect.Y + cornerRadius * 2, rect.Right, rect.Y + rect.Height - cornerRadius * 2);
//
//        roundedRect.AddArc(rect.X + rect.Width - cornerRadius * 2, rect.Y + rect.Height - cornerRadius * 2, cornerRadius * 2, cornerRadius * 2, 0, 90);
//        roundedRect.AddLine(rect.Right - cornerRadius * 2, rect.Bottom, rect.X + cornerRadius * 2, rect.Bottom);
//
//        roundedRect.AddArc(rect.X, rect.Bottom - cornerRadius * 2, cornerRadius * 2, cornerRadius * 2, 90, 90);
//        roundedRect.AddLine(rect.X, rect.Bottom - cornerRadius * 2, rect.X, rect.Y + cornerRadius * 2);
//        roundedRect.CloseFigure();
//        return roundedRect;
//    }
//
//    /**
//     * 
//     * 处理字符串的截取
//     *  source  即将进行截取操作的字符串
//     *  cellBounds  文字显示的区域
//     *  offsetLeft  文字的偏移长度，这个长度主要用来计入图片，间隙的宽度
//     *  replaceEnd  文字的尾部取代字符，在画出cellBounds以外的时候需要用到
//     *  graphics  用于计算字体的sizeF
//     *  font  显示的字体
//     * */
//    public static String GetClippedString(String source, Rectangle cellBounds, float offsetLeft, String replaceEnd, Font font)
//    {
//        String result = String.Empty;
//        SizeF sourceSize = TextRenderer.MeasureText(source, font);
//        //首先要计算，直接显示文本的时候是否可以直接完全显示。
//        if (sourceSize.Width + offsetLeft <= (float)cellBounds.Width)
//        {
//            result = source;
//        }
//        else
//        {
//            //文本的长度超出了cell的宽度
//            //开始寻找截断后的字符串结果
//            //@noted by longjiao 2011/10/23 12:57
//            int currLength = source.Length;
//            String tempResult = source + replaceEnd;
//            SizeF replaceSizeF = TextRenderer.MeasureText(replaceEnd, font);
//
//            while (currLength > 0)
//            {
//                //每次截取前面的currLength个字符，计算其SizeF
//                //移除索引为currLength-1处的字符
//                tempResult = tempResult.Remove(currLength - 1, 1);
//                SizeF CurrSizeF = TextRenderer.MeasureText(tempResult, font);
//
//                if (CurrSizeF.Width + offsetLeft <= (float)cellBounds.Width)
//                {
//                    //此时找到了位置
//                    int length = replaceEnd.Length;
//                    result = tempResult;
//                    break;
//                }
//                currLength--;
//            }
//            //执行到此的时候文本就不需要显示了
//        }
//        return result;
//    }
//
//    #region 批量编辑相关
//
//    /// <summary>
//    /// 开始批量编辑，目前只支持UDB
//    /// </summary>
//    /// <param name="recordset">目标记录集</param>
//    /// <param name="engineType">目标引擎类型</param>
//    /// <returns>返回是否支持批量编辑</returns>
//    public static boolean RecordsetBatchEditorBegin(Recordset recordset, EngineType engineType)
//    {
//        boolean isSupportBatchEditor = false;
//        try
//        {
//            Recordset.BatchEditor batchEditor = recordset.Batch;
//            if (batchEditor != null && engineType == EngineType.UDB)
//            {
//                isSupportBatchEditor = true;
//                batchEditor.MaxRecordCount = _Toolkit.MaxRecordCount;
//                batchEditor.Begin();
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//
//        return isSupportBatchEditor;
//    }
//
//    /// <summary>
//    /// 用指定几何对象更新记录集当前记录
//    /// </summary>
//    /// <param name="recordset">目标记录集</param>
//    /// <param name="geometry">待更新几何对象</param>
//    /// <param name="editHistory">历史记录</param>
//    /// <param name="isCurrentOnly">历史记录是否只添加当前对象</param>
//    /// <returns>更新成功的记录ID</returns>
//    public static int RecorsetSetGeometry(Recordset recordset, Geometry geometry, EditHistory editHistory, boolean isCurrentOnly)
//    {
//        int id = -1;
//        try
//        {
//            if (editHistory != null && recordset != null)
//            {
//                editHistory.Add(EditType.Modify, recordset, isCurrentOnly);
//                recordset.Edit();
//                if (recordset.SetGeometry(geometry)
//                    && recordset.Update())
//                {
//                    id = recordset.GetID();
//                }
//
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//
//        return id;
//    }
//
//    /// <summary>
//    /// 将指定几何对象添加到记录集中
//    /// </summary>
//    /// <param name="recordset">目标记录集</param>
//    /// <param name="geometry">待添加几何对象</param>
//    /// <param name="fieldValues">待添加记录的字段值</param>
//    /// <param name="editHistory">历史记录</param>
//    /// <param name="isBatchEditorEnable">批量编辑是否可用</param>
//    /// <param name="isCurrentOnly">历史记录是否只添加当前对象</param>
//    /// <returns>添加成功的记录ID</returns>
//    public static int RecorsetAddGeometry(Recordset recordset, Geometry geometry, Dictionary<String, Object> fieldValues, EditHistory editHistory, boolean isBatchEditorEnable, boolean isCurrentOnly)
//    {
//        int id = -1;
//        try
//        {
//            if (editHistory != null && recordset != null)
//            {
//                boolean isAddSuccess = false;
//                if (fieldValues != null)
//                {
//                    isAddSuccess = recordset.AddNew(geometry, fieldValues);
//                }
//                else
//                {
//                    isAddSuccess = recordset.AddNew(geometry);
//                }
//
//                if (isAddSuccess)
//                {
//                    if (!isBatchEditorEnable)
//                    {
//                        if (recordset.Update())
//                        {
//                            editHistory.Add(EditType.AddNew, recordset, true);
//                        }
//                    }
//
//                    id = recordset.GetID();
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//
//        return id;
//    }
//
//    /// <summary>
//    /// 更新记录集
//    /// </summary>
//    /// <param name="recordset">目标记录集</param>
//    /// <param name="addHistoryIDs">编辑添加的集合对象ID集合</param>
//    /// <param name="editHistory">历史记录</param>
//    /// <param name="isBatchEditorEnable">批量编辑是否可用</param>
//    public static void UpdateRecordset(Recordset recordset, List<int> addHistoryIDs, EditHistory editHistory, boolean isBatchEditorEnable)
//    {
//        try
//        {
//            if (isBatchEditorEnable
//                    && editHistory != null
//                    && recordset != null
//                    && addHistoryIDs != null
//                    && addHistoryIDs.Count > 0)
//            {
//                recordset.Batch.Update();
//                Recordset addHistoryRecordset = recordset.Dataset.Query(addHistoryIDs.ToArray(), CursorType.Dynamic);
//                editHistory.Add(EditType.AddNew, addHistoryRecordset, false);
//                CommonToolkit.ReleaseRecordset(ref addHistoryRecordset);
//            }
//        }
//        catch (Exception ex)
//        {
//            Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
//        }
//    }
//
//    #endregion
//
//    // 直接使用组件的枚举值，写法不标准，这里把不标准的重新字符串一下
//    public static String GetGeoSpheroidTypeName(GeoSpheroidType type)
//    {
//        String name = type.ToString();
//        name = name.Replace("Wgs", "WGS");
//        return name;
//    }
//
//    // 直接使用组件的枚举值，写法不标准，这里把不标准的重新字符串一下
//    public static String GetGeoDatumTypeName(GeoDatumType type)
//    {
//        String name = type.ToString();
//        name = name.Replace("Wgs", "WGS");
//        return name;
//    }
//
//    // 直接使用组件的枚举值，写法不标准，这里把不标准的重新字符串一下
//    public static String GetPrjCoordSysName(PrjCoordSysType type)
//    {
//        String name = type.ToString();
//        name = name.Replace("Wgs", "WGS");
//        return name;
//    }
//
//    // 直接使用组件的枚举值，写法不标准，这里把不标准的重新字符串一下
//    public static String GetGeoCoordSysName(GeoCoordSysType type)
//    {
//        String name = type.ToString();
//        switch (type)
//        {
//            case GeoCoordSysType.Wgs1966:
//                name = "WGS1966";
//                break;
//            case GeoCoordSysType.Wgs1972:
//                name = "WGS1972";
//                break;
//            case GeoCoordSysType.Wgs1972Be:
//                name = "WGS1972Be";
//                break;
//            case GeoCoordSysType.Wgs1984:
//                name = "WGS1984";
//                break;
//        }
//        return name;
//    }
//
//    //用于判断程序是否处于设计时，微软自带的DesignMode经查证是不靠谱的。
//    public static boolean IsDesignTime()
//    {
//        boolean result = false;
//        if (System.ComponentModel.LicenseManager.UsageMode == LicenseUsageMode.Designtime
//            || Process.GetCurrentProcess().ProcessName.ToLower().Equals(UI.Properties.ControlsResources.String_VSProcessName))
//        {
//            result = true;
//        }
//        return result;
//    }
//
//    public static void InvokeGeometryModifiedEvent(MapControl mapControl, GeometryEventArgs args)
//    {
//        if (mapControl != null && args != null)
//        {
//            mapControl.OnGeometryModified(args);
//        }
//    }
//
//    public static void InvokeGeometrySelectedEvent(MapControl mapControl, GeometrySelectedEventArgs args)
//    {
//        if (mapControl != null && args != null)
//        {
//            mapControl.OnGeometrySelected(args);
//        }
//    }
}
