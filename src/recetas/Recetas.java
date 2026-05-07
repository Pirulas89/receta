package recetas;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//hola

//pepardo
class ConnectionSingleton {
	private static Connection con;

	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://127.0.0.1:3307/Recetas";
		String user = "alumno";
		String password = "alumno";
		if (con == null || con.isClosed()) {
			con = DriverManager.getConnection(url, user, password);
		}
		return con;
	}
}

public class Recetas {

	private JFrame frame;
	private JTable tableIn;
	private JTextField txtIdIn;
	private JTextField txtNombreIn;
	private JTextField txtCalorias;
	private JTextField txtIdRe;
	private JTextField txtNombreRe;
	private JTextField txtTPrep;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Recetas window = new Recetas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Recetas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1021, 657);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("IdIn");
		model.addColumn("NombreIn");
		model.addColumn("CaloriasIn");
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Ingredientes");
			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("IdIngredientes");
				row[1] = rs.getString("NombreIn");
				row[2] = rs.getInt("Calorias");
				model.addRow(row);
			}
				rs.close();
				stmt.close();
				con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		
		tableIn = new JTable(model);
		tableIn.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollPane = new JScrollPane(tableIn);
		scrollPane.setBounds(48, 34, 292, 194);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblIdIn = new JLabel("Id Ingrediente");
		lblIdIn.setBounds(58, 247, 93, 17);
		frame.getContentPane().add(lblIdIn);
		
		txtIdIn = new JTextField();
		txtIdIn.setEditable(false);
		txtIdIn.setBounds(159, 245, 114, 21);
		frame.getContentPane().add(txtIdIn);
		txtIdIn.setColumns(10);
		
		JLabel lblNombreIn = new JLabel("Nombre");
		lblNombreIn.setBounds(68, 276, 60, 17);
		frame.getContentPane().add(lblNombreIn);
		
		txtNombreIn = new JTextField();
		txtNombreIn.setBounds(159, 278, 114, 21);
		frame.getContentPane().add(txtNombreIn);
		txtNombreIn.setColumns(10);
		
		JLabel lblCalorias = new JLabel("Calorias");
		lblCalorias.setBounds(68, 314, 60, 17);
		frame.getContentPane().add(lblCalorias);
		
		txtCalorias = new JTextField();
		txtCalorias.setBounds(159, 311, 114, 21);
		frame.getContentPane().add(txtCalorias);
		txtCalorias.setColumns(10);
		
	
		
		
		JButton btnMostrarIn = new JButton("Mostar");
		btnMostrarIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Ingredientes");
					while (rs.next()) {
						Object[] row = new Object[3];
						row[0] = rs.getInt("IdIngredientes");
						row[1] = rs.getString("NombreIn");
						row[2] = rs.getInt("Calorias");
						model.addRow(row);
					}
						rs.close();
						stmt.close();
						con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null , ex.getMessage());
					System.err.println(ex.getMessage());
				}
			}
		});
		btnMostrarIn.setBounds(159, 362, 105, 27);
		frame.getContentPane().add(btnMostrarIn);
		

		JButton btnAñadirIn = new JButton("Añadir");
		btnAñadirIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ins_psmt = con.prepareStatement("INSERT INTO Ingredientes (NombreIn, Calorias) VALUES (?,?)");
					ins_psmt.setString(1,txtNombreIn.getText());
					ins_psmt.setInt(2,Integer.parseInt(txtCalorias.getText()));
					ins_psmt.executeUpdate();
					ins_psmt.close();
					con.close();
					JOptionPane.showMessageDialog(null, "Ingrediente Añadido");
					btnMostrarIn.doClick();
				}catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null , ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		btnAñadirIn.setBounds(48, 362, 105, 27);
		frame.getContentPane().add(btnAñadirIn);
		
		JButton btnBorrarIn = new JButton("Borrar");
		btnBorrarIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement dele_pstmt = con.prepareStatement("DELETE FROM Ingredientes (idIngredientes) WHERE idI = ?");
					dele_pstmt.setInt(0,Integer.parseInt(txtIdIn.getText()));
					dele_pstmt.executeUpdate();
					dele_pstmt.close();
					con.close();
					JOptionPane.showMessageDialog(null, "Ingrediente Borrado");
					btnMostrarIn.doClick();
				}catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null , ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		btnBorrarIn.setBounds(48, 438, 105, 27);
		frame.getContentPane().add(btnBorrarIn);
		
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement upd_pstmt = con.prepareStatement("UPDATE * FROM Ingredientes");
					upd_pstmt.executeUpdate();
					upd_pstmt.close();
					con.close();
					JOptionPane.showMessageDialog(null, "Ingrediente Actualizado");
					btnMostrarIn.doClick();
				}catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null , ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		btnActualizar.setBounds(46, 401, 105, 27);
		frame.getContentPane().add(btnActualizar);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(362, 31, 339, 197);
		frame.getContentPane().add(scrollPane_1);
		
		JLabel lblIdRe = new JLabel("Id Receta");
		lblIdRe.setBounds(402, 247, 60, 17);
		frame.getContentPane().add(lblIdRe);
		
		txtIdRe = new JTextField();
		txtIdRe.setEditable(false);
		txtIdRe.setBounds(488, 245, 114, 21);
		frame.getContentPane().add(txtIdRe);
		txtIdRe.setColumns(10);
		
		JLabel lblNombreRe = new JLabel("Nombre");
		lblNombreRe.setBounds(402, 276, 60, 17);
		frame.getContentPane().add(lblNombreRe);
		
		txtNombreRe = new JTextField();
		txtNombreRe.setBounds(488, 274, 114, 21);
		frame.getContentPane().add(txtNombreRe);
		txtNombreRe.setColumns(10);
		
		JLabel lblTempPrep = new JLabel("Temp. Prep");
		lblTempPrep.setBounds(392, 314, 80, 17);
		frame.getContentPane().add(lblTempPrep);
		
		txtTPrep = new JTextField();
		txtTPrep.setBounds(488, 312, 114, 21);
		frame.getContentPane().add(txtTPrep);
		txtTPrep.setColumns(10);
		
		JButton btnAñadirRe = new JButton("Añadir");
		btnAñadirRe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnAñadirRe.setBounds(386, 362, 105, 27);
		frame.getContentPane().add(btnAñadirRe);
		
		JButton btnActualizarRe = new JButton("Actualizar");
		btnActualizarRe.setBounds(386, 401, 105, 27);
		frame.getContentPane().add(btnActualizarRe);
		
		JButton btnBorrarRe = new JButton("Borrar");
		btnBorrarRe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnBorrarRe.setBounds(386, 438, 105, 27);
		frame.getContentPane().add(btnBorrarRe);
		
		JButton btnMostrarRe = new JButton("Mostrar");
		btnMostrarRe.setBounds(503, 362, 105, 27);
		frame.getContentPane().add(btnMostrarRe);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(713, 34, 296, 194);
		frame.getContentPane().add(scrollPane_2);
		
		JButton btnAñadirMe = new JButton("Añadir");
		btnAñadirMe.setBounds(750, 271, 105, 27);
		frame.getContentPane().add(btnAñadirMe);
		
		JButton btnBorrarMe = new JButton("Borrar");
		btnBorrarMe.setBounds(750, 309, 105, 27);
		frame.getContentPane().add(btnBorrarMe);
		
		JButton btnMaxCalorias = new JButton("Max Calorias");
		btnMaxCalorias.setBounds(739, 348, 126, 27);
		frame.getContentPane().add(btnMaxCalorias);
		
		
	}

}
