package com.snl.tests;

import org.testng.annotations.Test;
import com.snl.services.Boardv2Service;

import io.restassured.response.Response;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

public class Boardv2Test {
	Response response = null;
	Boardv2Service boardObject = null;

	/**
	 * calling Boardv1Service class
	 */
	@BeforeTest
	public void call_class_Boardv1Service() {
		boardObject = new Boardv2Service();
	}

	/**
	 * Test to check if new board is created
	 * 
	 * @throws ParseException
	 */
	@Test(priority = 1)
	public void verify_if_the_service_call_for_new_board_executes() throws ParseException {
		response = boardObject.service_call_to_create_new_board();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * To check if the board list is returned
	 * 
	 * @throws ParseException
	 */
	@Test(priority = 2)
	public void verify_if_service_call_for_board_returns_all_boards() throws ParseException {
		response = boardObject.service_call_to_see_the_list_of_boards();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if the new player is added to board
	 */
	@Test(priority = 3)
	public void verify_if_new_player_is_added_to_the_board() {
		response = boardObject.service_call_to_add_new_player();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if the player moves to specific tile on board
	 */
	@Test(priority = 4)
	public void verify_if_the_player_moves_to_specific_tile_on_board() {
		response = boardObject.service_call_to_move_a_player();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if the player details added to board are correct
	 */
	@Test(priority = 5)
	public void verify_whether_player_details_are_correct() {
		response = boardObject.service_call_to_obtain_player_details();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if the player details are updated in board
	 */
	@Test(priority = 6)
	public void verify_whether_the_player_details_are_updated() {
		response = boardObject.service_call_to_update_player_details();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if the player details are deleted
	 */
	@Test(priority = 7)
	public void verify_whether_the_player_details_are_deleted() {
		response = boardObject.servie_call_to_delete_player_details();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if details of board with specific id is obtained
	 */
	@Test(priority = 8)
	public void verify_whether_the_id_board_details_are_obtained() {
		response = boardObject.servie_call_to_obtain_board_with_id();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if board details are reset
	 */
	@Test(priority = 9)
	public void verify_whether_the_board_is_reset() {
		response = boardObject.service_call_to_reset_board();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if player is removed from board
	 */
	@Test(priority = 10)
	public void verify_whether_the_player_is_deleted() {
		response = boardObject.service_call_to_delete_board();
		Assert.assertEquals(response.statusCode(), 200);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 200", true);
	}

	/**
	 * to verify if player is already absent from board
	 */
	@Test(priority = 11)
	public void verify_whether_delete_does_not_occur_for_absent_player() {
		response = boardObject.service_call_to_delete_absent_board();
		Assert.assertEquals(response.statusCode(), 500);
		Reporter.log("ASSERTION VERIFIED: STATUS CODE IS RETURNED 500", true);
	}
}
