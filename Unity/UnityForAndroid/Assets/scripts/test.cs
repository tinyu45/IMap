using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class test : MonoBehaviour {

	private Text mText;
	// Use this for initialization
	void Start () {
		//int ret = MyAddFunc(200, 200);
		//Debug.LogFormat("--- ret:{0}", ret);
		mText = GameObject.Find("MsgText").GetComponent<Text>();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	/***
	 * 打开周边搜索场景
	 * */
	public void toSearchPOL()
	{
		SceneManager.LoadScene ("Search");
	}


	public void MyShowDialog()
	{
		// Android的Java接口  
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		// 参数  
		string[] mObject = new string[2];
		mObject[0] = "对话框";
		mObject[1] = "Unity 调用 Android SDK成功...!";
		// 调用方法  
		string ret = jo.Call<string>("ShowDialog", mObject);
		setMsg(ref ret);
	}

	public void MyShowToast()
	{
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		jo.Call("ShowToast", "Showing on Toast");
	}

	/// <summary>
	/// 测试 unity->java->unity
	/// </summary>
	public void MyInteraction()
	{
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		jo.Call("callUnityFunc", "Main Camera", "BeCallFunc", "Unity---android");
	}



	public void ViewToMap()
	{
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		jo.Call("ViewToMap");
	}

	/***
	 * 获取当前位置描述
	 * */
	public void GETLocationFroAndroid()
	{
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		jo.Call("callgetLocation", "Main Camera", "BeCallFunc");
	}

	/***
	 * 获取搜索建议
	 * 
	 * */
	public void GetSearchSug()
	{
		SceneManager.LoadScene ("AutoSearch");
	}



	public void BeCallFunc(string _content)
	{
		setMsg(ref _content);
	}

	/***设置文本框内容***/
	private void setMsg(ref string _str)
	{
		mText.text = _str;
	}
}
