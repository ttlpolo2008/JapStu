package webservices;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.CommonDAO;
import dto.UserDto;
import enums.UserType;
import services.LearnService;
import services.UserService;
import util.Constants;

@Path("UserAPI")
public class UserAPI {

	@POST
	@Path("/findUserInfo.json")
	@Produces(MediaType.APPLICATION_JSON)  //kiểu dữ liệu nhận và trả về là Json
	public String findUserInfo(String requestBody) { //requestBody la String mà client truyền lên.
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser(); //jsonParser để parse String thành Json.
		JsonObject result = new JsonObject(); //kqua ma webservice sẽ trả về cho Client.

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject(); //client truyền lên string, thì sẽ parse qua JsonObject để sdung
			if (!body.has("userName") || !body.has("password")) { //ktra truyen du hang muc băt buộc chua?
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001); //

			} else {
				// Get LessonId
				String userName = body.get("userName").getAsString(); //lay du lieu username tu chuoi Parameter(Parameter kieu la Json)
				String password = body.get("password").getAsString();


				UserDto userDto = UserService.findUserInfo(userName, password); //search du lieu trong database, xem co giong voi parameter truyen vao
				if (userDto == null) {
					result.addProperty("ERR002", Constants.ERR_002);
				} else {
					Gson gson = new Gson();
					String jsonData = gson.toJson(userDto);	//parse gia tri tra ve sang chuoi String.
					result.add("data", jsonParser.parse(jsonData)); //tra het thong tin user ve client. gtri tra ve la Json
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}

	@POST
	@Path("/registerAccount.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String registerAccount(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("userName") || !body.has("password")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				// Create UserDto
				UserDto userDto = new UserDto();
				userDto.setUserName(body.get("userName").getAsString());
				userDto.setPassword(body.get("password").getAsString());
				userDto.setUserType(UserType.USER.getCode());
				if (body.has("nickName")) {
					userDto.setNickName(body.get("nickName").getAsString());
				}
				if (body.has("age")) {
					userDto.setAge(body.get("age").getAsInt());
				}
				if (body.has("profileImage")) {
					userDto.setProfileImage(body.get("profileImage").getAsString());
				}

				// Check userName existed
				if (UserService.findUserByUserName(userDto.getUserName()) != null) {
					result.addProperty("ERR999", Constants.ERR_003);
				} else {
					// Insert User
					if (!UserService.insertUser(userDto)
							|| !LearnService.createLearn(userDto.getUserId())) {
						CommonDAO.getDAO().rollback();
						result.addProperty("result", false);
					} else {
						CommonDAO.getDAO().commit();
						result.addProperty("result", true);
					}
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}
}
