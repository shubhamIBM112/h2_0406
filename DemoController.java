package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.demo.vo.Vo;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.GCPCalVo;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Controller
public class DemoController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/upload")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

		System.out.println("Hi");
		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			// parse CSV file to create a list of `User` objects
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

				// create csv bean reader
				CsvToBean<GCPCalVo> csvToBean = new CsvToBeanBuilder(reader).withType(GCPCalVo.class)
						.withIgnoreLeadingWhiteSpace(true).build();

				// convert `CsvToBean` object to list of users
				List<GCPCalVo> users = csvToBean.parse();

				for (int i = 0; i < users.size(); i++) {
					GCPCalVo gCPCalVo = new GCPCalVo();
					System.out.println("Commitment Type " + users.get(i).getCommitmentType());
					System.out.println("Memory " + users.get(i).getMemory());
					System.out.println("Location " + users.get(i).getLocation());
					System.out.println("CPU " + users.get(i).getvCPU());
					System.out.println("OS " + users.get(i).getOperatingSystem());
					System.out.println("InstanceType " + users.get(i).getInstanceType());
					//System.out.println("diskspace " + users.get(i).getDiskSpace());
					//Long totalram = Long.parseLong(users.get(i).getMemory());
					Double totalCPU = Double.parseDouble(users.get(i).getvCPU());
					Double totalmemory = Double.parseDouble(users.get(i).getMemory());
					System.out.println("totalmemory"+totalmemory);
					System.out.println("totalCPU"+totalCPU);
					Double finalAdd = 0.0;
					List <Double> l1 = new ArrayList<>();
					// users.set(i, gCPCalVo.setTotalcost("5000"));

					String query1 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'Compute' and categoryusageType =? and categoryresourceGroup ='RAM'";
					//String query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
					
					jdbcTemplate.queryForObject(query1,new Object[] {users.get(i).getLocation(),users.get(i).getCommitmentType()},new
							RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
									int rowNum) throws SQLException { Vo obj = new
									Vo();
							//String value = rs.getString("totalram");
									String value = "1";
							Double ramcost = Double.parseDouble(value);
							
							System.out.println("query1"+query1);
							System.out.println("Value1: "+value);
							//double d = Double.parseDouble(number);
							
							Double FinalRamcost = ramcost*730*totalmemory;
							//System.out.println("FinalCost: "+FinalRamcost);
							//int totoalcostram = ramcost*730*totalmemory;
							System.out.println("Total RAM Cost =" + FinalRamcost);
							l1.add(FinalRamcost);
							return obj; 
					}
			} );
					
					String query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'Compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
					//String query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
					
					jdbcTemplate.queryForObject(query2,new Object[] {users.get(i).getLocation(),users.get(i).getCommitmentType()},new
							RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
									int rowNum) throws SQLException { Vo obj = new
									Vo();
							//String value = rs.getString("totalcpu");
									String value = "2";
							Double cpucost = Double.parseDouble(value);
							System.out.println("query1"+query2);
							System.out.println("Value1: "+value);
							Double FinalCPUcost = cpucost*730*totalCPU;							
							System.out.println("Total CPU Cost =" + FinalCPUcost);
							l1.add(FinalCPUcost);
							return obj; 
					}
			} );
					Double finalValue = 0.0;
					for(int j =0;j<2;j++) {
						finalValue = finalValue+l1.get(j);
					}
					
					System.out.println("Final VAlue line 128 : "+finalValue);
					gCPCalVo.setTotalCost(finalValue);
					
					users.get(i).setTotalCost(finalValue);
				}
				
				// TODO: save users in DB?
				/*
				 * users.get(0).getCommitmentType();
				 * 
				 * System.out.println("Commitment Type "+users.get(0).getCommitmentType());
				 * System.out.println("Commitment Type "+users.get(0).getMemory());
				 * System.out.println("Commitment Type "+users.get(0).getLocation());
				 * System.out.println("Commitment Type "+users.get(0).getvCPU());
				 * System.out.println("Commitment Type "+users.get(0).getOperatingSystem());
				 * System.out.println("Commitment Type "+users.get(0).getInstanceType());
				 */

				// select
				// min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as
				// totalram from gcp_cal where serviceRegions0 ='us-central1' and
				// categoryresourceFamily = 'compute' and categoryusageType ="Commit1Yr" and
				// categoryresourceGroup ='RAM'
				// union
				// select
				// min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as
				// totalcpu from gcp_cal where serviceRegions0 ='us-central1' and
				// categoryresourceFamily = 'compute' and categoryusageType ="Commit1Yr" and
				// categoryresourceGroup ='CPU'

				// save users list on model
				model.addAttribute("users", users);
				// model.addAttribute("users", );

				model.addAttribute("status", true);

			} catch (Exception ex) {
				model.addAttribute("message", "An error occurred while processing the CSV file.");
				model.addAttribute("status", false);
			}
		}

		return "file-upload-status";
	}

	@PostMapping("/abc")
	public String saveNewLaunchPlan(@RequestBody Map<String, String> vo) {
		return "Shubhm";
	}
	
	
	
}