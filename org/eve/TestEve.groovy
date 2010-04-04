package org.eve

class TestEve {
	static void main(String[] args) {
		def eve = new Eve(userID:'aaa', apiKey:'bbb')
		println eve.skillTree()
	}
}
