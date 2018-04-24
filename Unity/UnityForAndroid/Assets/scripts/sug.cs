using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class sug : MonoBehaviour {

	private Transform SugParent;
	private List<GameObject> textList;
	public InputField input;
	public GameObject ScrollView;

	AndroidJavaClass jc ;
	AndroidJavaObject jo;


	// Use this for initialization
	void Start () {
		jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		textList = new List<GameObject> ();
		SugParent = GameObject.Find ("Content").transform;
		//ScrollView.SetActive (false);
		if (SugParent == null) 
		{
			print ("sugparent is null");
		}
	}
	
	// Update is called once per frame
	void Update () 
	{
		if (Input.GetKey (KeyCode.Escape)) 
		{
			UnityEngine.SceneManagement.SceneManager.LoadScene ("UnityForAndroid");
		}		
	}



	/***
	 * 获取搜索建议
	 * 
	 * */

	/***设置文本框内容***/
	private void ChangeSugList(ref string _str)
	{
		if (_str.Length == 0) 
		{
			jo.Call("ShowToast", "无相关内容");
		} 
		else 
		{
			string[] sugs=_str.Split ('|');

			jo.Call("ShowToast", _str);

			for (int k = 0; k < SugParent.childCount; k++) 
			{
				textList [k].GetComponent<Text> ().text = sugs [k];
			}

			if (SugParent.childCount < sugs.Length) {
				int start = SugParent.childCount == 0 ? 0 : SugParent.childCount - 1;
					for (int i = start; i < sugs.Length; i++)
					GenSugText (sugs[i], i);
			}

			else if(SugParent.childCount > sugs.Length)
			{
				int i = SugParent.childCount - 1;
				while (i == sugs.Length || i > sugs.Length) 
				{
					Destroy(textList [i]);
					i--;
				}
			}
		}
	}


	void GetMsg(string msg)
	{
		ChangeSugList (ref msg);
	}


	/**
	 * 搜索框 内容改变时
	 * */
	public void  OnInputFiledVlaueChanged(string value)
	{
		
		if (value.Length != 0) 
		{
			jo.Call("SugSearch", "Main Camera", "GetMsg", value);
		}

		/**
		print (SugParent.childCount);
		if (SugParent.childCount == 0) {
			for (int i = 0; i < 10; i++) {
				GenSugText (value + "" + i, i);
			}
		}
		else
		{
			for (int i = 0; i < textList.Count; i++) 
			{
				textList [i].GetComponent<Text> ().text = value + "" + i;
			}
		}
		**/

	}



	void GenSugText(string value, int index, float minTop=0, float textHeight=200)
	{
		GameObject sug=GameObject.Instantiate(Resources.Load("sugtext"), SugParent) as GameObject;
		sug.GetComponent<RectTransform>().localPosition=new Vector3 (0, minTop-index*textHeight, 0);
		sug.GetComponent<Text> ().text = value;
		sug.GetComponent<Button> ().onClick.AddListener (delegate() {
			this.OnsugTextClcik (sug.GetComponent<Text> ().text);
		});
		textList.Add (sug);
	}


	/***
	 * sugText click
	 * */
	public void OnsugTextClcik(string value)
	{
		print (value);
		input.text = value;
		ScrollView.SetActive (false);
	}

}
