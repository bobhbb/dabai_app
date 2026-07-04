package com.dabai.app.service.auth

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("current_user_id")
        private val KEY_WECHAT_OPEN_ID = stringPreferencesKey("wechat_open_id")
        private val KEY_USER_NICKNAME = stringPreferencesKey("user_nickname")
        private val KEY_USER_AVATAR = stringPreferencesKey("user_avatar")
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")

        // WeChat SDK 配置（需在腾讯开放平台申请）
        const val WECHAT_APP_ID = "wx_your_app_id_here"
        const val WECHAT_APP_SECRET = "your_app_secret_here"
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID] != null
    }

    val currentUserId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    val currentUserInfo: Flow<Map<String, String?>> = context.dataStore.data.map { prefs ->
        mapOf(
            "userId" to prefs[KEY_USER_ID],
            "openId" to prefs[KEY_WECHAT_OPEN_ID],
            "nickname" to prefs[KEY_USER_NICKNAME],
            "avatar" to prefs[KEY_USER_AVATAR]
        )
    }

    /**
     * 微信登录处理
     *
     * 生产环境流程：
     * 1. 调用 WeChat SDK 的 WXEntryActivity 处理回调
     * 2. 获取 code → 用 code 向自建后端换 token
     * 3. 后端用 code + appSecret 调微信接口获取 openId / sessionKey
     * 4. 存入本地 DataStore
     *
     * 此处为演示占位，模拟微信登录返回用户信息
     */
    suspend fun loginWithWeChat(code: String): AuthResult {
        // 演示：微信授权登录模拟
        val mockOpenId = "mock_wechat_open_id_${System.currentTimeMillis()}"
        val mockNickname = "大白用户_${(1000..9999).random()}"
        val mockAvatar = ""

        saveAuthData(mockOpenId, mockNickname, mockAvatar, "mock_access_token")

        return AuthResult(
            success = true,
            userId = context.dataStore.data.first()[KEY_USER_ID] ?: "",
            openId = mockOpenId,
            nickname = mockNickname,
            avatarUrl = mockAvatar,
            errorMessage = null
        )
    }

    /**
     * 发送微信登录授权请求
     * 实际项目通过 WeChat SDK 唤起微信
     */
    fun sendWeChatAuthRequest() {
        // -----------------------------------------------------------------
        // 生产环境：
        // val req = SendAuth.Req()
        // req.scope = "snsapi_userinfo"
        // req.state = "dabai_wechat_login"
        // api.sendReq(req)
        // -----------------------------------------------------------------

        // 演示：发送广播/Intent 让 MainActivity 处理
        val intent = Intent("com.dabai.app.WECHAT_AUTH").apply {
            putExtra("scope", "snsapi_userinfo")
            putExtra("state", "dabai_wechat_login")
        }
        context.sendBroadcast(intent)
    }

    private suspend fun saveAuthData(
        openId: String,
        nickname: String,
        avatar: String,
        token: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = java.util.UUID.randomUUID().toString()
            prefs[KEY_WECHAT_OPEN_ID] = openId
            prefs[KEY_USER_NICKNAME] = nickname
            prefs[KEY_USER_AVATAR] = avatar
            prefs[KEY_ACCESS_TOKEN] = token
        }
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }

    data class AuthResult(
        val success: Boolean,
        val userId: String = "",
        val openId: String = "",
        val nickname: String = "",
        val avatarUrl: String = "",
        val errorMessage: String? = null
    )
}
