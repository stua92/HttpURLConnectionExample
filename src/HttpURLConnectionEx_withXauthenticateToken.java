

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpURLConnectionEx_withXauthenticateToken {

	public static void main(String[] args) throws IOException {
		URL url = new URL("https://localhost:8080/api/...");
        
        // 문자열로 URL 표현
        System.out.println("URL :" + url.toExternalForm());
        
        // HTTP Connection 구하기 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setDoOutput(true); 
        
        // 요청 방식 설정 ( GET or POST or .. 별도로 설정하지않으면 GET 방식 )
        conn.setRequestMethod("POST"); 
        
        // 연결 타임아웃 설정 
        conn.setConnectTimeout(30000); // 30초 
        
        // 읽기 타임아웃 설정 
        conn.setReadTimeout(30000); // 30초 
      
        // params 영역
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("id", "stua92");
        
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        // 추가 요청 부분 (헤더에 인증 토큰을 추가하여 서비스를 호출합니다.)
        // headers 영역
        conn.setRequestProperty("X-Authenticate-Token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55Q29kZSI6IjUwMDAiLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJnZXJwIl0sInVzZXJpZCI6InN0dWE5MiIsImp0aSI6IjE2N2ZjZmQ1LTIyOWItNDdjNy1iMjM4LWFjYTJlODY5NDQ3ZCIsImNsaWVudF9pZCI6ImdlcnAiLCJncm91cENvZGUiOiIxMDAwIn0.6E6zcz1Rf4KtXZGeG1auOe527UV85fDrmX4nzAjwM90");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        
        // 연결
        conn.connect();
        
        // 요청 방식 구하기
        System.out.println("getRequestMethod():" + conn.getRequestMethod());
        // 응답 콘텐츠 유형 구하기
        System.out.println("getContentType():" + conn.getContentType());
        // 응답 코드 구하기
        System.out.println("getResponseCode():"    + conn.getResponseCode());
        // 응답 메시지 구하기
        System.out.println("getResponseMessage():" + conn.getResponseMessage());
        
        
        // 응답 헤더의 정보를 모두 출력
        for (Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
            for (String value : header.getValue()) {
                System.out.println(header.getKey() + " : " + value);
            }
        }
        
        // 응답 내용(BODY) 구하기        
        try (InputStream in = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            System.out.println(new String(out.toByteArray(), "UTF-8"));            
        }
        
        // 접속 해제
        conn.disconnect();
	}
}