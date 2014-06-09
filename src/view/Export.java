package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.End;
import model.JoinGateway;
import model.ObjectBPMN;
import model.Pool;
import model.SplitGateway;
import model.Start;
import model.Task;
import model.WorkFlow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Export {

	private static void printDocument(Document doc, StreamResult out)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), out);
	}

	static private class SequenceFlow {

		public String getSourceRef() {
			return sourceRef;
		}

		public String getTargetRef() {
			return targetRef;
		}

		public int getSourceX() {
			return sourceX;
		}

		public int getSourceY() {
			return sourceY;
		}

		public int getTargetX() {
			return targetX;
		}

		public int getTargetY() {
			return targetY;
		}

		public boolean isSourceSet() {
			return sourceSet;
		}

		public boolean isTargetSet() {
			return targetSet;
		}

		public void setSourceRef(String sourceRef) {
			this.sourceRef = sourceRef;
		}

		public void setTargetRef(String targetRef) {
			this.targetRef = targetRef;
		}

		public void setSourceX(int sourceX) {
			this.sourceX = sourceX;
		}

		public void setSourceY(int sourceY) {
			this.sourceY = sourceY;
		}

		public void setTargetX(int targetX) {
			this.targetX = targetX;
		}

		public void setTargetY(int targetY) {
			this.targetY = targetY;
		}

		public void setSourceSet(boolean sourceSet) {
			this.sourceSet = sourceSet;
		}

		public void setTargetSet(boolean targetSet) {
			this.targetSet = targetSet;
		}

		private String sourceRef;
		private String targetRef;
		private int sourceX;
		private int sourceY;
		private int targetX;
		private int targetY;
		private boolean sourceSet;
		private boolean targetSet;
	}

	public void bpmnExport(WorkFlow wf, File f) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element definitionsElement = doc.createElement("model:definitions");
		doc.appendChild(definitionsElement);

		definitionsElement.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		definitionsElement.setAttribute("xmlns:bonitaConnector",
				"http://www.bonitasoft.org/studio/connector/definition/6.0");
		definitionsElement.setAttribute("xmlns:dc",
				"http://www.omg.org/spec/DD/20100524/DC");
		definitionsElement.setAttribute("xmlns:di",
				"http://www.omg.org/spec/BPMN/20100524/DI");
		definitionsElement.setAttribute("xmlns:di_1",
				"http://www.omg.org/spec/DD/20100524/DI");
		definitionsElement.setAttribute("xmlns:java",
				"http://jcp.org/en/jsr/detail?id=270");
		definitionsElement.setAttribute("xmlns:model",
				"http://www.omg.org/spec/BPMN/20100524/MODEL");
		definitionsElement
				.setAttribute("xsi:schemaLocation",
						"schemaLocation http://www.omg.org/spec/BPMN/20100524/MODEL schemas/BPMN20.xsd");
		definitionsElement.setAttribute("exporter", "BPMNTool");
		definitionsElement.setAttribute("exporterVersion", "1.0.0");
		definitionsElement.setAttribute("expressionLanguage",
				"http://groovy.codehaus.org/");
		definitionsElement.setAttribute("targetNamespace",
				"http://bonitasoft.com/BPMNToolDiagram");

		Element collaborationElement = doc.createElement("model:collaboration");
		definitionsElement.appendChild(collaborationElement);

		collaborationElement.setAttribute("id", "BPMNToolDiagram");

		Element diagramElement = doc.createElement("di:BPMNDiagram");

		// TODO: nom du fichier
		diagramElement.setAttribute("name", f.getName());

		Element planeElement = doc.createElement("di:BPMNPlane");
		diagramElement.appendChild(planeElement);

		planeElement.setAttribute("id", "plane__BPMNToolDiagram");
		planeElement.setAttribute("bpmnElement", "BPMNToolDiagram");

		Map<Map.Entry<Integer, Integer>, SequenceFlow> previousSequenceFlows = new HashMap<Map.Entry<Integer, Integer>, SequenceFlow>();

		for (Pool pool : wf.getPools()) {
			String poolId = "pool" + Integer.toString(pool.getId());
			String poolLaneId = "poolLane" + Integer.toString(pool.getId());
			Map<Map.Entry<Integer, Integer>, SequenceFlow> sequenceFlows = new HashMap<Map.Entry<Integer, Integer>, SequenceFlow>(
					previousSequenceFlows);

			Element poolParticipantElement = doc
					.createElement("model:participant");
			collaborationElement.appendChild(poolParticipantElement);

			poolParticipantElement.setAttribute("id", "participant_" + poolId);
			poolParticipantElement.setAttribute("name", pool.getLabel());
			poolParticipantElement.setAttribute("processRef", poolId);

			Element processElement = doc.createElement("model:process");
			definitionsElement.appendChild(processElement);

			processElement.setAttribute("id", poolId);
			processElement.setAttribute("name", pool.getLabel());

			Element laneSetElement = doc.createElement("model:laneSet");
			processElement.appendChild(laneSetElement);

			Element laneElement = doc.createElement("model:lane");
			laneSetElement.appendChild(laneElement);

			laneElement.setAttribute("id", poolLaneId);
			laneElement.setAttribute("name", pool.getLabel());

			Element poolShapeElement = doc.createElement("di:BPMNShape");
			planeElement.appendChild(poolShapeElement);

			poolShapeElement.setAttribute("id", "shape_" + poolId);
			poolShapeElement.setAttribute("bpmnElement", poolId);
			poolShapeElement.setAttribute("isHorizontal", "true");

			Element poolShapeBoundsElement = doc.createElement("dc:Bounds");
			poolShapeElement.appendChild(poolShapeBoundsElement);

			poolShapeBoundsElement.setAttribute("height",
					Integer.toString(pool.getH()));
			poolShapeBoundsElement.setAttribute("width",
					Integer.toString(pool.getL()));
			poolShapeBoundsElement.setAttribute("x", "0");
			poolShapeBoundsElement.setAttribute("y",
					Integer.toString(pool.getY()));

			Element poolLaneShapeElement = doc.createElement("di:BPMNShape");
			planeElement.appendChild(poolLaneShapeElement);

			poolLaneShapeElement.setAttribute("id", "shape_" + poolLaneId);
			poolLaneShapeElement.setAttribute("bpmnElement", poolLaneId);
			poolLaneShapeElement.setAttribute("isHorizontal", "true");

			Element poolLaneShapeBoundsElement = doc.createElement("dc:Bounds");
			poolLaneShapeElement.appendChild(poolLaneShapeBoundsElement);

			poolLaneShapeBoundsElement.setAttribute("height",
					Integer.toString(pool.getH()));
			poolLaneShapeBoundsElement.setAttribute("width",
					Integer.toString(pool.getL()));
			poolLaneShapeBoundsElement.setAttribute("x", "0");
			poolLaneShapeBoundsElement.setAttribute("y",
					Integer.toString(pool.getY()));

			for (ObjectBPMN obj : pool.getObjects()) {
				String objId = "obj" + Integer.toString(obj.getId());

				Element objElement = null;
				if (obj instanceof Task) {
					objElement = doc.createElement("model:userTask");
				} else if (obj instanceof Start) {
					objElement = doc.createElement("model:startEvent");
				} else if (obj instanceof End) {
					objElement = doc.createElement("model:endEvent");
				} else if (obj instanceof SplitGateway) {
					objElement = doc.createElement("model:parallelGateway");
				} else if (obj instanceof JoinGateway) {
					objElement = doc.createElement("model:exclusiveGateway");
				} else {
					throw new ParserConfigurationException("Unknown oject type");
				}
				processElement.appendChild(objElement);

				Element flowNodeRefElement = doc
						.createElement("model:flowNodeRef");
				laneElement.appendChild(flowNodeRefElement);

				flowNodeRefElement.appendChild(doc.createTextNode(objId));

				objElement.setAttribute("id", objId);
				objElement.setAttribute("name", obj.getLabel());

				Element objShapeElement = doc.createElement("di:BPMNShape");
				planeElement.appendChild(objShapeElement);

				objShapeElement.setAttribute("id", "shape_" + objId);
				objShapeElement.setAttribute("bpmnElement", objId);

				Element objShapeBoundsElement = doc.createElement("dc:Bounds");
				objShapeElement.appendChild(objShapeBoundsElement);

				objShapeBoundsElement.setAttribute("height",
						Integer.toString(obj.getH()));
				objShapeBoundsElement.setAttribute("width",
						Integer.toString(obj.getL()));
				objShapeBoundsElement.setAttribute("x",
						Integer.toString(obj.getX()));
				objShapeBoundsElement.setAttribute("y",
						Integer.toString(obj.getY()));

				for (Integer sourceId : obj.getLinks_arrivant()) {
					Map.Entry<Integer, Integer> key = new AbstractMap.SimpleEntry<Integer, Integer>(
							sourceId, obj.getId());
					SequenceFlow sequenceFlow = sequenceFlows.get(key);
					if (sequenceFlow == null) {
						sequenceFlow = new SequenceFlow();
						sequenceFlows.put(key, sequenceFlow);
						sequenceFlow.setSourceRef("obj" + sourceId);
						sequenceFlow.setTargetRef(objId);
						sequenceFlow.setSourceX(0);
						sequenceFlow.setSourceY(0);
						sequenceFlow.setSourceSet(false);
					}

					sequenceFlow.setTargetX(obj.getX());
					sequenceFlow.setTargetY(obj.getY() + (obj.getH() / 2));
					sequenceFlow.setTargetSet(true);
				}

				for (Integer targetId : obj.getLinks_partant()) {
					Map.Entry<Integer, Integer> key = new AbstractMap.SimpleEntry<Integer, Integer>(
							obj.getId(), targetId);
					SequenceFlow sequenceFlow = sequenceFlows.get(key);
					if (sequenceFlow == null) {
						sequenceFlow = new SequenceFlow();
						sequenceFlows.put(key, sequenceFlow);
						sequenceFlow.setSourceRef(objId);
						sequenceFlow.setTargetRef("obj" + targetId);
						sequenceFlow.setTargetX(0);
						sequenceFlow.setTargetY(0);
						sequenceFlow.setTargetSet(false);
					}

					sequenceFlow.setSourceX(obj.getX() + obj.getL());
					sequenceFlow.setSourceY(obj.getY() + (obj.getH() / 2));
					sequenceFlow.setSourceSet(true);
				}
			}

			previousSequenceFlows.clear();

			for (Map.Entry<Map.Entry<Integer, Integer>, SequenceFlow> entry : sequenceFlows
					.entrySet()) {
				Map.Entry<Integer, Integer> key = entry.getKey();
				SequenceFlow sequenceFlow = entry.getValue();

				if (!sequenceFlow.isSourceSet() || !sequenceFlow.isTargetSet()) {
					previousSequenceFlows.put(key, sequenceFlow);
					continue;
				}

				String sequenceFlowId = "seq_" + sequenceFlow.getSourceRef()
						+ "_" + sequenceFlow.getTargetRef();

				Element sequenceFlowElement = doc
						.createElement("model:sequenceFlow");
				processElement.appendChild(sequenceFlowElement);

				sequenceFlowElement.setAttribute("id", sequenceFlowId);
				sequenceFlowElement.setAttribute("sourceRef",
						sequenceFlow.getSourceRef());
				sequenceFlowElement.setAttribute("targetRef",
						sequenceFlow.getTargetRef());

				Element sequenceFlowEdgeElement = doc
						.createElement("di:BPMNEdge");
				planeElement.appendChild(sequenceFlowEdgeElement);

				sequenceFlowEdgeElement.setAttribute("id", "shape_"
						+ sequenceFlowId);
				sequenceFlowEdgeElement.setAttribute("bpmnElement",
						sequenceFlowId);

				Element sourceWayPointElement = doc
						.createElement("di_1:waypoint");
				sequenceFlowEdgeElement.appendChild(sourceWayPointElement);

				sourceWayPointElement.setAttribute("x",
						Integer.toString(sequenceFlow.getSourceX()));
				sourceWayPointElement.setAttribute("y",
						Integer.toString(sequenceFlow.getSourceY()));

				Element targetWayPointElement = doc
						.createElement("di_1:waypoint");
				sequenceFlowEdgeElement.appendChild(targetWayPointElement);

				targetWayPointElement.setAttribute("x",
						Integer.toString(sequenceFlow.getTargetX()));
				targetWayPointElement.setAttribute("y",
						Integer.toString(sequenceFlow.getTargetY()));

			}
		}

		definitionsElement.appendChild(diagramElement);

		// End of creation

		// write the content into xml file
		StreamResult result = new StreamResult(f);

		// Output to console for testing
		// StreamResult result = new StreamResult(new OutputStreamWriter(
		// System.out, "UTF-8"));

		printDocument(doc, result);

		// System.out.println("File saved!");

	}

	public void jsonExport(WorkFlow wf, File f) throws Exception {
		ObjectMapper mapper2 = new ObjectMapper();
		FileWriter writer = null;
		try {
			String s = mapper2.writeValueAsString(wf);
			writer = new FileWriter(f);
			writer.write(s);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public WorkFlow jsonImport(File f) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			String encoding = "UTF8";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), encoding));
			String str;
			StringBuffer strBuffer = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				strBuffer.append(str);
			}
			reader.close();
			return mapper.readValue(strBuffer.toString(), WorkFlow.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
