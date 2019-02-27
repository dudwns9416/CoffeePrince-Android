package com.sc.coffeeprince.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Order {
	

	private Integer id;
	private String orderId;
	private Integer total = 1;
	private Integer resultCode; // 1.주문 확인 요망 2.주문 확인 완료 3.음료 제조 완료  4.손님이 음료 취득 5.결제 취소
	private User users;
	private Menus menus;
	private Cafe cafe;
    private String contents;
	private String date;
	private String uid;

	public Order() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public Menus getMenus() {
		return menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
	}

	public Cafe getCafe() {
		return cafe;
	}

	public void setCafe(Cafe cafe) {
		this.cafe = cafe;
	}

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		String result = null;
		try {
			result = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
