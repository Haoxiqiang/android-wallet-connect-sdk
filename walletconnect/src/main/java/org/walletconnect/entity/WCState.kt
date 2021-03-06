package org.walletconnect.entity

import org.json.JSONArray
import org.json.JSONObject
import org.walletconnect.impls.toList

data class WCState(
	val config: WCConfig,
	val clientData: PeerData,
	val peerData: PeerData?,
	val topic: String?,
	val currentKey: String,
	val approvedAccounts: List<String>?,
	val chainId: Long?
) {

	fun toJSON(): JSONObject {
		val json = JSONObject()
		json.put("config", config.toJSON())
		json.put("clientData", clientData.toJSON())
		peerData?.let {
			json.put("peerData", peerData.toJSON())
		}
		json.put("topic", topic)
		json.put("currentKey", currentKey)
		approvedAccounts?.let { list ->
			json.put("approvedAccounts", JSONArray(list))
		}
		json.put("chainId", chainId)
		return json
	}

	companion object {

		fun fromJSON(json: JSONObject): WCState? {
			if (!json.has("topic")) {
				return null
			}
			val topic = json.getString("topic")
			val chainId = json.getLong("chainId")
			val currentKey = json.getString("currentKey")
			val approvedAccounts: List<String> = json.getJSONArray("approvedAccounts").toList()
			val clientData = PeerData.fromJSON(json.getJSONObject("clientData"))
			val peerData =
				json.optJSONObject("peerData")?.let { PeerData.fromJSON(it) }
			val config = WCConfig.fromJSON(json.getJSONObject("config"))

			return WCState(
				config = config,
				clientData = clientData,
				peerData = peerData,
				topic = topic,
				currentKey = currentKey,
				approvedAccounts = approvedAccounts,
				chainId = chainId
			)
		}
	}
}