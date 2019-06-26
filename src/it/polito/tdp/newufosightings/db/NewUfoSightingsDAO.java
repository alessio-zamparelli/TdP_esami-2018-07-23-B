package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings(int anno, Map<String, State> stateIdMap) {
		String sql = "SELECT * FROM sighting WHERE YEAR(datetime)=?";
		List<Sighting> list = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				State stato = stateIdMap.get(res.getString("state").toUpperCase());
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), stato, res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> loadAllBorders(Map<String, State> stateIdMap) {
		String sql = "SELECT state1, state2 FROM neighbor";
		List<Border> result = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State s1 = stateIdMap.get(rs.getString("state1"));
				State s2 = stateIdMap.get(rs.getString("state2"));

				result.add(new Border(s1, s2));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

	}

	public int getWeight(int anno, int maxDays, State s1, State s2) {
		String sql = "SELECT COUNT(*) FROM sighting s1, sighting s2 WHERE YEAR(s1.datetime) = ? "
				+ "AND YEAR(s2.datetime) = ? AND s1.state = ? AND s2.state = ? "
				+ "AND s1.datetime < s2.datetime AND DATEDIFF(s1.datetime, s2.datetime) < ?";
		int tot = 0;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setString(3, s1.getId());
			st.setString(4, s2.getId());
			st.setInt(5, maxDays);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				tot = rs.getInt(1);
			}

			conn.close();
			return tot;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public class Border {
		private State s1;
		private State s2;

		public Border(State s1, State s2) {
			this.s1 = s1;
			this.s2 = s2;
		}

		public State getS1() {
			return this.s1;
		}

		public State getS2() {
			return this.s2;
		}
	}

}
