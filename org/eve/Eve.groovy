/*******************************************************************************
* EvE
* Copyright (C) year  name of author
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3.0 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package org.eve


class Eve {
	String userID
	String apiKey
	String characterID

	def eveCall = { method, userID, apiKey, characterID ->
		def postBody = [
		    userID:userID,
		    apiKey:apiKey,
		    characterID:characterID
		]
		def url = new URL("http://api.eveonline.com/${method}")
		def conn = url.openConnection()
		conn.setRequestMethod('POST')

		String body = null
		postBody.each { k,v ->
		    body = (body == null? "${k}=${v}" : body + "&${k}=${v}")
		}

		conn.doOutput = true

		Writer wr = new OutputStreamWriter(conn.outputStream)
		wr.write(body)
		wr.flush()
		wr.close()

		conn.connect()
		return conn.content.text
	}

	static def eve_methods = [
		// General purpose methods (do not requiere parameters)
		mapData:'map/Sovereignty.xml.aspx',
		skillTree:'eve/SkillTree.xml.aspx',
		refTypes:'eve/RefTypes.xml.aspx',
		// User methods (only userID and apiKey required)
		characters:'account/Characters.xml.aspx',
		// Character methods (userID, apiKey, characterID)
		characterSheet:'char/CharacterSheet.xml.aspx',
		skillTraining:'char/SkillInTraining.xml.aspx',
		memberTrackingData:'corp/MemberTracking.xml.aspx',
		accountBalance:'char/AccountBalance.xml.aspx',
		corpBalance:'corp/AccountBalance.xml.aspx'
	]
	
	static {
		Eve.eve_methods.each { k,method ->
			Eve.metaClass."$k" = {
		    	delegate.eveCall(method, delegate.userID, delegate.apiKey, delegate.characterID)
			}
		}
	}
}

