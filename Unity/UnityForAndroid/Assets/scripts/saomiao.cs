using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class saomiao : MonoBehaviour {

	// Use this for initialization
	private Image img;
	private float timer;
	private bool isfirst = true;
	private int index=0;
	private float pretude;
	private Transform range;

	public Text poi;
	private string[] pois;


	void Awake()
	{
		getLocalPoi ();
	}

	void Start () {
		img = GetComponent<Image> ();
		timer = 0;
		range=GameObject.Find ("Range").transform;
	}



	// Update is called once per frame
	void Update () 
	{

		if (Input.GetKey (KeyCode.Escape)) 
		{
			UnityEngine.SceneManagement.SceneManager.LoadScene ("UnityForAndroid");
		}

		if(pretude==0)
			pretude = pois.Length == 0 ? 30 : 360 / pois.Length;
		timer++;

		if (timer % pretude == 0) 
		{
			poi.text = pois [index]+timer;

			if (isfirst)
			{
				GenerateBall (timer/180*Mathf.PI); //参数为弧度值
			}

			index++;
		}

		if (timer >= 360) 
		{
			isfirst = false;
			timer = 0;
			index = 0;
		}
			
		img.fillAmount = 1 - timer / 360;
	}	


	/***
	 * 生成小球
	 * */
	void GenerateBall(float tude, float minR=80, float maxR=480, float posZ=-50)
	{
		float len=Random.Range(minR, maxR);
		Vector3 pos=new Vector3(-Mathf.Sin(tude)*len, Mathf.Cos(tude)*len, posZ);
		/**
		Object obj = Resources.Load ("ball");
		GameObject gobj=GameObject.Instantiate (obj) as GameObject;
		**/
		GameObject ball=GameObject.Instantiate (Resources.Load ("ball"),range) as GameObject;
		ball.transform.localPosition = pos;
	}



	/***设置文本框内容***/
	public void BeCallFunc(string _content)
	{
		setMsg(ref _content);
	}

	private void setMsg(ref string _str)
	{
		pois = _str.Split ('|');
		//poi.text=_str;
	}

	/**
	 * 获取周边POI
	 * */
	public void getLocalPoi()
	{
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
		jo.Call("callgetPOI", "Range", "BeCallFunc");
	}

}



